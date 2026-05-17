package ac.gachon.iot.service;

import ac.gachon.iot.domain.entity.ControlLog;
import ac.gachon.iot.domain.entity.Device;
import ac.gachon.iot.domain.entity.EnergyDaily;
import ac.gachon.iot.domain.entity.Room;
import ac.gachon.iot.domain.enums.ControlMode;
import ac.gachon.iot.domain.enums.DeviceStatus;
import ac.gachon.iot.domain.repository.ControlLogRepository;
import ac.gachon.iot.domain.repository.DeviceRepository;
import ac.gachon.iot.domain.repository.EnergyDailyRepository;
import ac.gachon.iot.dto.EnergyDailyResponse;
import ac.gachon.iot.dto.EnergyEfficiencyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EnergyServiceTest {

    @Mock ControlLogRepository controlLogRepository;
    @Mock EnergyDailyRepository energyDailyRepository;
    @Mock DeviceRepository deviceRepository;

    @InjectMocks EnergyService energyService;

    private Room room;
    private Device light;   // 50W
    private Device aircon;  // 1500W
    private OffsetDateTime todayStart;

    @BeforeEach
    void setUp() {
        room = mock(Room.class);
        light = mock(Device.class);
        given(light.getId()).willReturn(1L);
        given(light.getPowerWatt()).willReturn(50);
        aircon = mock(Device.class);
        given(aircon.getId()).willReturn(2L);
        given(aircon.getPowerWatt()).willReturn(1500);
        todayStart = LocalDate.now().atStartOfDay().atOffset(ZoneOffset.UTC);
    }

    @Test
    @DisplayName("오늘 control_log가 없으면 totalWh = 0")
    void findDailyUsage_noLogs_returnsZero() {
        EnergyDaily today = energyDailyOf(0.0, todayStart);
        given(energyDailyRepository.findByDate(LocalDate.now())).willReturn(Optional.of(today));
        given(controlLogRepository.findByCreatedAtBetween(any(), any())).willReturn(List.of());
        given(energyDailyRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        EnergyDailyResponse result = energyService.findDailyUsage();

        assertThat(result.getTotalWh()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("조명 1시간 사용 → 0.05 kWh")
    void findDailyUsage_lightOnOneHour_returns0_05kWh() {
        OffsetDateTime onTime  = OffsetDateTime.now().minusHours(2);
        OffsetDateTime offTime = OffsetDateTime.now().minusHours(1);

        EnergyDaily today = energyDailyOf(0.0, todayStart);
        given(energyDailyRepository.findByDate(LocalDate.now())).willReturn(Optional.of(today));
        given(controlLogRepository.findByCreatedAtBetween(any(), any())).willReturn(List.of(
                logOf(DeviceStatus.ON,  onTime),
                logOf(DeviceStatus.OFF, offTime)
        ));
        given(energyDailyRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        EnergyDailyResponse result = energyService.findDailyUsage();

        // 50W × 1h = 50Wh = 0.05 kWh
        assertThat(result.getTotalWh()).isCloseTo(0.05, org.assertj.core.data.Offset.offset(0.001));
    }

    @Test
    @DisplayName("에어컨 1.5시간 사용 → 2.25 kWh")
    void findDailyUsage_airconOneAndHalfHour_returns2_25kWh() {
        OffsetDateTime onTime  = OffsetDateTime.now().minusMinutes(90);
        OffsetDateTime offTime = OffsetDateTime.now();

        EnergyDaily today = energyDailyOf(0.0, todayStart);
        given(energyDailyRepository.findByDate(LocalDate.now())).willReturn(Optional.of(today));
        given(controlLogRepository.findByCreatedAtBetween(any(), any())).willReturn(List.of(
                airconLogOf(DeviceStatus.ON,  onTime),
                airconLogOf(DeviceStatus.OFF, offTime)
        ));
        given(energyDailyRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        EnergyDailyResponse result = energyService.findDailyUsage();

        // 1500W × 1.5h = 2250Wh = 2.25 kWh
        assertThat(result.getTotalWh()).isCloseTo(2.25, org.assertj.core.data.Offset.offset(0.01));
    }

    @Test
    @DisplayName("ON만 있고 OFF 없는 기기 → 현재까지 사용량 계산")
    void findDailyUsage_onlyOnLog_calculatesUntilNow() {
        OffsetDateTime onTime = OffsetDateTime.now().minusMinutes(30);

        EnergyDaily today = energyDailyOf(0.0, todayStart);
        given(energyDailyRepository.findByDate(LocalDate.now())).willReturn(Optional.of(today));
        given(controlLogRepository.findByCreatedAtBetween(any(), any())).willReturn(List.of(
                logOf(DeviceStatus.ON, onTime)
        ));
        given(energyDailyRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        EnergyDailyResponse result = energyService.findDailyUsage();

        // 50W × 0.5h = 25Wh = 0.025 kWh
        assertThat(result.getTotalWh()).isCloseTo(0.025, org.assertj.core.data.Offset.offset(0.002));
    }

    @Test
    @DisplayName("energy_daily 레코드 없으면 새로 생성 후 계산")
    void findDailyUsage_noEnergyDaily_createsAndCalculates() {
        EnergyDaily newRecord = energyDailyOf(0.0, todayStart);
        given(energyDailyRepository.findByDate(LocalDate.now())).willReturn(Optional.empty());
        given(energyDailyRepository.save(any())).willReturn(newRecord);
        given(controlLogRepository.findByCreatedAtBetween(any(), any())).willReturn(List.of());

        EnergyDailyResponse result = energyService.findDailyUsage();

        assertThat(result.getTotalWh()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("사용량 0이면 절감률 100%")
    void findEfficiency_noUsage_returns100() {
        EnergyDaily today = energyDailyOf(0.0, todayStart);
        given(energyDailyRepository.findByDate(LocalDate.now())).willReturn(Optional.of(today));
        given(controlLogRepository.findByCreatedAtBetween(any(), any())).willReturn(List.of());
        given(deviceRepository.findTotalMaxWhPerDay()).willReturn(1550.0); // 50 + 1500
        given(energyDailyRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        EnergyEfficiencyResponse result = energyService.findTotalMaxWhPerDay();

        assertThat(result.getEfficiency()).isEqualTo(100L);
    }

    @Test
    @DisplayName("조명 1시간 사용 시 절감률 계산")
    void findEfficiency_lightOneHour_correctEfficiency() {
        OffsetDateTime onTime  = OffsetDateTime.now().minusHours(2);
        OffsetDateTime offTime = OffsetDateTime.now().minusHours(1);

        EnergyDaily today = energyDailyOf(0.0, todayStart);
        given(energyDailyRepository.findByDate(LocalDate.now())).willReturn(Optional.of(today));
        given(controlLogRepository.findByCreatedAtBetween(any(), any())).willReturn(List.of(
                logOf(DeviceStatus.ON,  onTime),
                logOf(DeviceStatus.OFF, offTime)
        ));
        given(deviceRepository.findTotalMaxWhPerDay()).willReturn(1550.0); // 50 + 1500
        given(energyDailyRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        EnergyEfficiencyResponse result = energyService.findTotalMaxWhPerDay();

        // actualWh=50, maxWh=1550 → (1550-50)/1550*100 = 96.77 → round → 97%
        assertThat(result.getEfficiency()).isEqualTo(97L);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private EnergyDaily energyDailyOf(double totalWh, OffsetDateTime lastCalculatedAt) {
        return EnergyDaily.builder()
                .id(1L)
                .date(LocalDate.now())
                .totalWh(totalWh)
                .lastCalculatedAt(lastCalculatedAt)
                .build();
    }

    private ControlLog logOf(DeviceStatus action, OffsetDateTime createdAt) {
        return ControlLog.builder()
                .id(1L)
                .room(room)
                .device(light)
                .action(action)
                .type(ControlMode.MANUAL)
                .createdAt(createdAt)
                .build();
    }

    private ControlLog airconLogOf(DeviceStatus action, OffsetDateTime createdAt) {
        return ControlLog.builder()
                .id(2L)
                .room(room)
                .device(aircon)
                .action(action)
                .type(ControlMode.MANUAL)
                .createdAt(createdAt)
                .build();
    }
}
