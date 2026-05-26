package ac.gachon.iot.service;

import ac.gachon.iot.domain.entity.ControlLog;
import ac.gachon.iot.domain.entity.Device;
import ac.gachon.iot.domain.entity.Room;
import ac.gachon.iot.domain.enums.ControlMode;
import ac.gachon.iot.domain.enums.DeviceStatus;
import ac.gachon.iot.domain.repository.ControlLogRepository;
import ac.gachon.iot.domain.repository.DeviceRepository;
import ac.gachon.iot.domain.repository.RoomDeviceRepository;
import ac.gachon.iot.domain.repository.RoomRepository;
import ac.gachon.iot.dto.ControlLogResponse;
import ac.gachon.iot.dto.ControlMessage;
import ac.gachon.iot.dto.CreateControlLogRequest;
import ac.gachon.iot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final RoomDeviceRepository roomDeviceRepository;
    private final SimpMessagingTemplate messagingTemplate;

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

        ControlLog saved = controlLogRepository.save(
                ControlLog.builder()
                        .room(room)
                        .device(device)
                        .action(deviceStatus)
                        .type(ControlMode.MANUAL)
                        .build()
        );
        updateRoomDeviceStatus(room, device, deviceStatus);
        return ControlLogResponse.from(saved);

    }

    public void autoControl(Room room, Device device, DeviceStatus action) {
        controlLogRepository.save(
                ControlLog.builder()
                        .room(room)
                        .device(device)
                        .action(action)
                        .type(ControlMode.AUTO)
                        .build()
        );
        updateRoomDeviceStatus(room, device, action);
        log.info("Auto control executed. room={}, device={}, action={}", room.getName(), device.getName(), action);

        messagingTemplate.convertAndSend("/topic/rooms/" + room.getId() + "/control",
                ControlMessage.builder()
                        .roomId(room.getId())
                        .deviceName(device.getName())
                        .action(action.name())
                        .type("AUTO")
                        .build()
        );
    }

    private void updateRoomDeviceStatus(Room room, Device device, DeviceStatus status) {
        roomDeviceRepository.findByRoomAndDevice(room, device)
                .ifPresent(rd -> {
                    rd.updateStatus(status);
                    roomDeviceRepository.save(rd);
                });
    }

}
