package ac.gachon.iot.service;

import ac.gachon.iot.domain.entity.AlertLog;
import ac.gachon.iot.domain.entity.Sensor;
import ac.gachon.iot.domain.entity.SensorData;
import ac.gachon.iot.domain.enums.AlertType;
import ac.gachon.iot.domain.enums.SensorStatus;
import ac.gachon.iot.domain.repository.AlertLogRepository;
import ac.gachon.iot.domain.repository.SensorDataRepository;
import ac.gachon.iot.domain.repository.SensorRepository;
import ac.gachon.iot.dto.MqttSensorPayload;
import ac.gachon.iot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataIngestionService {

    private final SensorRepository sensorRepository;
    private final SensorDataRepository sensorDataRepository;
    private final AlertLogRepository alertLogRepository;

    @Value("${sensor.threshold.temperature.max:30.0}")
    private BigDecimal temperatureMax;

    @Value("${sensor.threshold.humidity.max:80.0}")
    private BigDecimal humidityMax;

    @Transactional
    public void ingest(String identifier, MqttSensorPayload payload) {
        Sensor sensor = sensorRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new NotFoundException("Sensor not found: " + identifier));

        SensorStatus status = determineStatus(payload);
        sensorDataRepository.save(SensorData.create(sensor, payload, status));

        if (status == SensorStatus.ANOMALY) {
            AlertType alertType = resolveAlertType(payload);
            String message = resolveAlertMessage(alertType, payload);
            alertLogRepository.save(AlertLog.create(sensor, alertType, message));
            log.warn("Anomaly detected. identifier={}, alertType={}", identifier, alertType);
        }

        log.info("SensorData ingested. identifier={}, status={}", identifier, status);
    }

    private SensorStatus determineStatus(MqttSensorPayload payload) {
        if (payload.temperature() != null && payload.temperature().compareTo(temperatureMax) > 0) {
            return SensorStatus.ANOMALY;
        }
        if (payload.humidity() != null && payload.humidity().compareTo(humidityMax) > 0) {
            return SensorStatus.ANOMALY;
        }
        if (payload.motion() != null && payload.motion() == 1 && isNightTime()) {
            return SensorStatus.ANOMALY;
        }
        return SensorStatus.NORMAL;
    }

    private boolean isNightTime() {
        int hour = OffsetDateTime.now().getHour();
        return hour >= 22 || hour < 6;
    }

    private AlertType resolveAlertType(MqttSensorPayload payload) {
        if (payload.temperature() != null && payload.temperature().compareTo(temperatureMax) > 0) {
            return AlertType.HIGH_TEMP;
        }
        if (payload.humidity() != null && payload.humidity().compareTo(humidityMax) > 0) {
            return AlertType.HIGH_HUMIDITY;
        }
        return AlertType.NIGHT_MOTION;
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
