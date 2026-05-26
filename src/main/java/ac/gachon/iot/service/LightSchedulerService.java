package ac.gachon.iot.service;

import ac.gachon.iot.domain.enums.DeviceStatus;
import ac.gachon.iot.domain.repository.DeviceRepository;
import ac.gachon.iot.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LightSchedulerService {

    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;
    private final ControlService controlService;

    @Scheduled(cron = "0 0 7 * * MON-FRI", zone = "Asia/Seoul")
    public void turnOnLightsOnWeekdayMorning() {
        log.info("Scheduled: 평일 오전 7시 조명 ON");
        controlAllRoomLights(DeviceStatus.ON);
    }

    @Scheduled(cron = "0 0 18 * * MON-FRI", zone = "Asia/Seoul")
    public void turnOffLightsOnWeekdayEvening() {
        log.info("Scheduled: 평일 오후 6시 조명 OFF");
        controlAllRoomLights(DeviceStatus.OFF);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeLightStatus() {
        DeviceStatus action = isBusinessHour() ? DeviceStatus.ON : DeviceStatus.OFF;
        log.info("서버 시작 시 조명 초기화: {}", action);
        controlAllRoomLights(action);
    }

    private boolean isBusinessHour() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        DayOfWeek day = now.getDayOfWeek();
        int hour = now.getHour();
        boolean isWeekday = day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
        return isWeekday && hour >= 7 && hour < 18;
    }

    private void controlAllRoomLights(DeviceStatus action) {
        try {
            deviceRepository.findByName("조명").ifPresent(light ->
                    roomRepository.findAll().forEach(room -> {
                        try {
                            controlService.autoControl(room, light, action);
                        } catch (Exception e) {
                            log.error("조명 제어 실패. room={}, action={}", room.getName(), action, e);
                        }
                    })
            );
        } catch (Exception e) {
            log.error("조명 일괄 제어 중 오류 발생. action={}", action, e);
        }
    }
}
