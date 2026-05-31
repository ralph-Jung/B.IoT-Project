package ac.gachon.iot.service;

import ac.gachon.iot.domain.entity.AlertLog;
import ac.gachon.iot.domain.entity.Sensor;
import ac.gachon.iot.domain.entity.SensorData;
import ac.gachon.iot.domain.enums.AlertType;
import ac.gachon.iot.domain.enums.DeviceStatus;
import ac.gachon.iot.domain.enums.SensorStatus;
import ac.gachon.iot.domain.repository.AlertLogRepository;
import ac.gachon.iot.domain.repository.DeviceRepository;
import ac.gachon.iot.domain.repository.SensorDataRepository;
import ac.gachon.iot.domain.repository.SensorRepository;
import ac.gachon.iot.dto.MqttSensorPayload;
import ac.gachon.iot.dto.SensorDataMessage;
import ac.gachon.iot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataIngestionService {

    private final SensorRepository sensorRepository;
    private final SensorDataRepository sensorDataRepository;
    private final AlertLogRepository alertLogRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ControlService controlService;
    private final DeviceRepository deviceRepository;
    private final AlertEmailService alertEmailService;

    @Value("${sensor.threshold.temperature.max:30.0}")
    private BigDecimal temperatureMax;

    @Value("${sensor.threshold.humidity.max:80.0}")
    private BigDecimal humidityMax;

    @Transactional
    public void ingest(String identifier, MqttSensorPayload payload) {
        Sensor sensor = sensorRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new NotFoundException("Sensor not found: " + identifier));

        SensorStatus status = determineStatus(payload, sensor.getSensorType().getName());
        sensorDataRepository.save(SensorData.create(sensor, payload, status));

        List<Runnable> postCommitActions = new ArrayList<>();

        if (status == SensorStatus.ANOMALY) {
            List<AlertType> alertTypes = resolveAlertTypes(payload, sensor.getSensorType().getName());
            for (AlertType alertType : alertTypes) {
                String alertMessage = resolveAlertMessage(alertType, payload);
                alertLogRepository.save(AlertLog.create(sensor, alertType, alertMessage));
                postCommitActions.add(() -> alertEmailService.sendAlert(identifier, alertType, alertMessage));
                log.warn("Anomaly detected. identifier={}, alertType={}, message={}", identifier, alertType, alertMessage);
            }
        }

        log.info("SensorData ingested. identifier={}, status={}", identifier, status);

        SensorDataMessage message = SensorDataMessage.builder()
                .sensorId(sensor.getId())
                .roomId(sensor.getRoom().getId())
                .sensorIdentifier(sensor.getIdentifier())
                .sensorType(sensor.getSensorType().getName())
                .temperature(payload.temperature())
                .humidity(payload.humidity())
                .motion(payload.motion())
                .status(status.name())
                .createdAt(OffsetDateTime.now())
                .build();

        // 트랜잭션 커밋 후 외부 알림 발행 — 롤백 시 알림이 발송되지 않도록 보장
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                messagingTemplate.convertAndSend("/topic/rooms/" + message.getRoomId(), message);
                if (status == SensorStatus.ANOMALY) {
                    messagingTemplate.convertAndSend("/topic/rooms/" + message.getRoomId() + "/alerts", message);
                }
                postCommitActions.forEach(Runnable::run);
            }
        });

        // 자동 제어는 센서 저장 트랜잭션과 분리 — 제어 실패가 센서 저장에 영향 주지 않음
        tryAutoControl(sensor, payload, status);
    }

    private void tryAutoControl(Sensor sensor, MqttSensorPayload payload, SensorStatus status) {
        String sensorTypeName = sensor.getSensorType().getName();
        try {
            if ("PIR".equals(sensorTypeName) && payload.motion() != null) {
                DeviceStatus lightAction = payload.motion() == 1 ? DeviceStatus.ON : DeviceStatus.OFF;
                deviceRepository.findByName("조명").ifPresent(light ->
                        controlService.autoControl(sensor.getRoom(), light, lightAction));
            }

            if (status == SensorStatus.ANOMALY && "DHT22".equals(sensorTypeName)) {
                List<AlertType> alertTypes = resolveAlertTypes(payload, sensorTypeName);
                boolean hasThermalAnomaly = alertTypes.stream()
                        .anyMatch(t -> t == AlertType.HIGH_TEMP || t == AlertType.HIGH_HUMIDITY);
                if (hasThermalAnomaly) {
                    deviceRepository.findByName("에어컨").ifPresent(aircon ->
                            controlService.autoControl(sensor.getRoom(), aircon, DeviceStatus.ON));
                }
            } else if ("DHT22".equals(sensorTypeName)) {
                boolean tempOk = payload.temperature() == null || payload.temperature().compareTo(temperatureMax) <= 0;
                boolean humOk  = payload.humidity()    == null || payload.humidity().compareTo(humidityMax) <= 0;
                if (tempOk && humOk) {
                    deviceRepository.findByName("에어컨").ifPresent(aircon ->
                            controlService.autoControl(sensor.getRoom(), aircon, DeviceStatus.OFF));
                }
            }
        } catch (Exception e) {
            log.error("Auto control failed. identifier={}, sensorType={}", sensor.getIdentifier(), sensorTypeName, e);
        }
    }

    private SensorStatus determineStatus(MqttSensorPayload payload, String sensorType) {
        if ("DHT22".equals(sensorType)) {
            if (payload.temperature() != null && payload.temperature().compareTo(temperatureMax) > 0) {
                return SensorStatus.ANOMALY;
            }
            if (payload.humidity() != null && payload.humidity().compareTo(humidityMax) > 0) {
                return SensorStatus.ANOMALY;
            }
        }
        if ("PIR".equals(sensorType)) {
            if (payload.motion() != null && payload.motion() == 1 && isNightTime()) {
                return SensorStatus.ANOMALY;
            }
        }
        return SensorStatus.NORMAL;
    }

    private boolean isNightTime() {
        int hour = OffsetDateTime.now(ZoneId.of("Asia/Seoul")).getHour();
        return hour >= 22 || hour < 6;
    }

    private List<AlertType> resolveAlertTypes(MqttSensorPayload payload, String sensorType) {
        List<AlertType> types = new ArrayList<>();
        if ("DHT22".equals(sensorType)) {
            if (payload.temperature() != null && payload.temperature().compareTo(temperatureMax) > 0) {
                types.add(AlertType.HIGH_TEMP);
            }
            if (payload.humidity() != null && payload.humidity().compareTo(humidityMax) > 0) {
                types.add(AlertType.HIGH_HUMIDITY);
            }
        }
        if ("PIR".equals(sensorType) && payload.motion() != null && payload.motion() == 1 && isNightTime()) {
            types.add(AlertType.NIGHT_MOTION);
        }
        return types;
    }

    private String resolveAlertMessage(AlertType alertType, MqttSensorPayload payload) {
        return switch (alertType) {
            case HIGH_TEMP -> "온도가 임계값(%.1f°C)을 초과했습니다. 현재: %s°C"
                    .formatted(temperatureMax, payload.temperature());
            case HIGH_HUMIDITY -> "습도가 임계값(%.1f%%)을 초과했습니다. 현재: %s%%"
                    .formatted(humidityMax, payload.humidity());
            case NIGHT_MOTION -> "야간 모션이 감지되었습니다.";
        };
    }
}
