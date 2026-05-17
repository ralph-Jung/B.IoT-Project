package ac.gachon.iot.service;

import ac.gachon.iot.domain.enums.DeviceStatus;
import ac.gachon.iot.domain.repository.AlertLogRepository;
import ac.gachon.iot.domain.repository.RoomDeviceRepository;
import ac.gachon.iot.domain.repository.SensorRepository;
import ac.gachon.iot.dto.DashBoardSummaryResponse;
import ac.gachon.iot.dto.EnergyEfficiencyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DashBoardServiceTest {

    @Mock SensorRepository sensorRepository;
    @Mock AlertLogRepository alertLogRepository;
    @Mock RoomDeviceRepository roomDeviceRepository;
    @Mock EnergyService energyService;

    @InjectMocks DashBoardService dashBoardService;

    @Test
    @DisplayName("정상 케이스 - 모든 데이터 정상 반환")
    void getSummary_normalCase() {
        given(sensorRepository.count()).willReturn(5L);
        given(alertLogRepository.countByCreatedAtBetween(any(), any())).willReturn(3L);
        given(roomDeviceRepository.findCurrentTotalWatt(eq(DeviceStatus.ON))).willReturn(1550L);
        given(energyService.findTotalMaxWhPerDay()).willReturn(
                EnergyEfficiencyResponse.builder().efficiency(48L).build()
        );

        DashBoardSummaryResponse result = dashBoardService.getSummary();

        assertThat(result.getTotalSenorCount()).isEqualTo(5);
        assertThat(result.getTotalAlertCount()).isEqualTo(3);
        assertThat(result.getCurrentTotalWatt()).isEqualTo(1550L);
        assertThat(result.getEfficiency()).isEqualTo(48L);
    }

    @Test
    @DisplayName("ON 상태 기기 없으면 소비전력 0 반환")
    void getSummary_noOnDevices_returnsZeroPower() {
        given(sensorRepository.count()).willReturn(5L);
        given(alertLogRepository.countByCreatedAtBetween(any(), any())).willReturn(0L);
        given(roomDeviceRepository.findCurrentTotalWatt(eq(DeviceStatus.ON))).willReturn(null);
        given(energyService.findTotalMaxWhPerDay()).willReturn(
                EnergyEfficiencyResponse.builder().efficiency(100L).build()
        );

        DashBoardSummaryResponse result = dashBoardService.getSummary();

        assertThat(result.getCurrentTotalWatt()).isEqualTo(0L);
    }

    @Test
    @DisplayName("오늘 이상 건수 없으면 0 반환")
    void getSummary_noTodayAlerts_returnsZeroAlertCount() {
        given(sensorRepository.count()).willReturn(3L);
        given(alertLogRepository.countByCreatedAtBetween(any(), any())).willReturn(0L);
        given(roomDeviceRepository.findCurrentTotalWatt(eq(DeviceStatus.ON))).willReturn(500L);
        given(energyService.findTotalMaxWhPerDay()).willReturn(
                EnergyEfficiencyResponse.builder().efficiency(70L).build()
        );

        DashBoardSummaryResponse result = dashBoardService.getSummary();

        assertThat(result.getTotalAlertCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("센서 없으면 totalSensorCount 0 반환")
    void getSummary_noSensors_returnsZeroSensorCount() {
        given(sensorRepository.count()).willReturn(0L);
        given(alertLogRepository.countByCreatedAtBetween(any(), any())).willReturn(0L);
        given(roomDeviceRepository.findCurrentTotalWatt(eq(DeviceStatus.ON))).willReturn(null);
        given(energyService.findTotalMaxWhPerDay()).willReturn(
                EnergyEfficiencyResponse.builder().efficiency(100L).build()
        );

        DashBoardSummaryResponse result = dashBoardService.getSummary();

        assertThat(result.getTotalSenorCount()).isEqualTo(0);
    }
}
