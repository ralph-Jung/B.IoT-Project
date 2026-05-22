package ac.gachon.iot.service;

import ac.gachon.iot.domain.entity.ControlLog;
import ac.gachon.iot.domain.entity.Device;
import ac.gachon.iot.domain.entity.Room;
import ac.gachon.iot.domain.enums.ControlMode;
import ac.gachon.iot.domain.enums.DeviceStatus;
import ac.gachon.iot.domain.repository.ControlLogRepository;
import ac.gachon.iot.domain.repository.DeviceRepository;
import ac.gachon.iot.domain.repository.RoomRepository;
import ac.gachon.iot.dto.ControlLogResponse;
import ac.gachon.iot.dto.CreateControlLogRequest;
import ac.gachon.iot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ControlService {

    private final ControlLogRepository controlLogRepository;
    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;

    // 모든 제어 이력 가져오기
    public List<ControlLogResponse> findAllControlLogs() {

        List<ControlLog> allLogs = controlLogRepository.findAllControlLog();

        return allLogs.stream()
                .map(log ->
                        ControlLogResponse.builder()
                                .roomName(log.getRoom().getName())
                                .deviceName(log.getDevice().getName())
                                .type(log.getType().name())
                                .action(log.getAction().name())
                                .createdAt(log.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm:ss"))).build()
                ).toList();

    }

    // 제어 이력 추가하기
    public ControlLogResponse createControlLog(CreateControlLogRequest request) {
        //  해당 room 객체 찾아오기
        Room room = roomRepository.findById(Long.parseLong(request.getRoom_id())).orElseThrow(() -> new NotFoundException("해당 Room 정보를 찾을 수 없습니다."));

        // 해당 device 객체 찾아오기
        Device device = deviceRepository.findById(Long.parseLong(request.getDevice_id())).orElseThrow(() -> new NotFoundException("해당 Device 정보를 찾을 수 없습니다."));

        DeviceStatus deviceStatus = (request.getAction().equals("ON") ? DeviceStatus.ON : DeviceStatus.OFF);

        ControlMode controlMode = ControlMode.MANUAL;
        ControlLog saved = controlLogRepository.save(
                ControlLog.builder()
                        .room(room)
                        .device(device)
                        .action(deviceStatus)
                        .type(controlMode)
                        .build()
        );

        // saved 를 return 할 수도 있지만 controller 까지 controlLog 라는 엔티티를 보여주는 것은 좋은 설계가 아니므로 DTO로 감싸서 주기
        // from 메서드를 static 으로 지정했기 때문에 클래스를 통해서 바로 접근이 가능하다
        // 이런 변환 팩토리 메서드는 항상 static 으로 정의하는게 관례
        return ControlLogResponse.from(saved);

    }

}
