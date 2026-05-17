package ac.gachon.iot.service;

import ac.gachon.iot.domain.entity.ControlLog;
import ac.gachon.iot.domain.entity.EnergyDaily;
import ac.gachon.iot.domain.enums.DeviceStatus;
import ac.gachon.iot.domain.repository.ControlLogRepository;
import ac.gachon.iot.domain.repository.EnergyDailyRepository;
import ac.gachon.iot.dto.EnergyDailyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnergyService {

    private final ControlLogRepository controlLogRepository;
    private final EnergyDailyRepository energyDailyRepository;

    public EnergyDailyResponse findDailyUsage() {
        EnergyDaily energyDaily = getOrCreateToday();
        Double addedWh = calculateAdditional(energyDaily);
        return updateAndReturn(energyDaily, addedWh);
    }


    // 누적량 가져오기
    private EnergyDaily getOrCreateToday() {
        return energyDailyRepository.findByDate(LocalDate.now())
                .orElseGet(() -> energyDailyRepository.save(
                        EnergyDaily.builder()
                                .date(LocalDate.now())
                                .totalWh(0.0)
                                .lastCalculatedAt(OffsetDateTime.now())
                                .build()));
    }

    private Double calculateAdditional(EnergyDaily today) {
        OffsetDateTime start = today.getLastCalculatedAt();
        OffsetDateTime end = OffsetDateTime.now();

        // lastCalculatedAt ~ 현재 사이 이력 전체 조회
        List<ControlLog> logs = controlLogRepository.findByCreatedAtBetween(start, end);

        double addedWh = 0.0;

        for (ControlLog log : logs) {
            if (log.getAction() == DeviceStatus.OFF) {

                // logs 안에서 쌍이 되는 ON 찾기
                Optional<ControlLog> onLog = logs.stream()
                        .filter(l -> l.getDevice().equals(log.getDevice())
                                && l.getRoom().equals(log.getRoom())
                                && l.getAction() == DeviceStatus.ON
                                && l.getCreatedAt().isBefore(log.getCreatedAt()))
                        .findFirst();

                // logs 안에 ON이 없으면 DB에서 범위 밖 ON 조회
                OffsetDateTime onTime = onLog
                        .map(ControlLog::getCreatedAt)
                        .orElseGet(() ->
                                controlLogRepository
                                        .findTopByRoomAndDeviceOrderByCreatedAtDesc(
                                                log.getRoom(), log.getDevice())
                                        .map(ControlLog::getCreatedAt)
                                        .orElse(start)  // 그것도 없으면 start로 대체
                        );

                // ON ~ OFF 시간 계산 → Wh 변환
                double hours = Duration.between(onTime, log.getCreatedAt())
                        .toSeconds() / 3600.0;
                addedWh += hours * log.getDevice().getPowerWatt();
            }
        }

        // 현재 ON 상태인 기기 계산 (OFF 없이 ON만 있는 것)
        List<ControlLog> onLogs = logs.stream()
                .filter(l -> l.getAction() == DeviceStatus.ON)
                .filter(l -> logs.stream()
                        .noneMatch(off -> off.getDevice().equals(l.getDevice())
                                && off.getRoom().equals(l.getRoom())
                                && off.getAction() == DeviceStatus.OFF
                                && off.getCreatedAt().isAfter(l.getCreatedAt())))
                .toList();

        for (ControlLog l : onLogs) {
            double hours = Duration.between(l.getCreatedAt(), end)
                    .toSeconds() / 3600.0;
            addedWh += hours * l.getDevice().getPowerWatt();
        }

        return addedWh;
    }

    private EnergyDailyResponse updateAndReturn(EnergyDaily today, Double addedWh) {
        today.setTotalWh(today.getTotalWh() + addedWh);
        today.setLastCalculatedAt(OffsetDateTime.now());
        energyDailyRepository.save(today);

        return EnergyDailyResponse.builder()
                .totalWh(today.getTotalWh() / 1000)
                .date(today.getLastCalculatedAt())
                .build();
    }

}
