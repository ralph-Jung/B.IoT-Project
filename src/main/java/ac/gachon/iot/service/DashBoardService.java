package ac.gachon.iot.service;

import ac.gachon.iot.domain.enums.DeviceStatus;
import ac.gachon.iot.domain.repository.AlertLogRepository;
import ac.gachon.iot.domain.repository.RoomDeviceRepository;
import ac.gachon.iot.domain.repository.SensorRepository;
import ac.gachon.iot.dto.DashBoardSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final SensorRepository sensorRepository;
    private final AlertLogRepository alertLogRepository;
    private final RoomDeviceRepository roomDeviceRepository;
    private final EnergyService energyService;

    public DashBoardSummaryResponse getSummary() {
        Integer totalSensorCount = (int) sensorRepository.count();

        OffsetDateTime todayStart = LocalDate.now().atStartOfDay().atOffset(ZoneOffset.UTC);
        Integer todayAlertCount = (int) alertLogRepository.countByCreatedAtBetween(todayStart, OffsetDateTime.now());

        Long currentTotalWatt = roomDeviceRepository.findCurrentTotalWatt(DeviceStatus.ON);
        if (currentTotalWatt == null) currentTotalWatt = 0L;

        Long efficiency = energyService.findTotalMaxWhPerDay().getEfficiency();

        return DashBoardSummaryResponse.builder()
                .totalSenorCount(totalSensorCount)
                .totalAlertCount(todayAlertCount)
                .currentTotalWatt(currentTotalWatt)
                .efficiency(efficiency)
                .build();
    }
}
