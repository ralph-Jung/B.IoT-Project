package ac.gachon.iot.controller;

import ac.gachon.iot.domain.repository.RoomDeviceRepository;
import ac.gachon.iot.dto.AllRoomsResponse;
import ac.gachon.iot.dto.RoomDeviceStatusResponse;
import ac.gachon.iot.dto.RoomSensorLatestResponse;
import ac.gachon.iot.service.RoomService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomDeviceRepository roomDeviceRepository;

    @GetMapping("")
    public List<AllRoomsResponse> getAllRooms() {
        return roomService.findAllRooms();
    }

    @GetMapping("{roomId}/sensors/latest")
    public RoomSensorLatestResponse getLatestSensorsByRoom(@Parameter(description = "방 ID", required = true)
                                                           @PathVariable Long roomId) {
        return roomService.findLatestSensorsByRoom(roomId);
    }

    @GetMapping("{roomId}/devices")
    public List<RoomDeviceStatusResponse> getRoomDeviceStatuses(@PathVariable Long roomId) {
        return roomDeviceRepository.findByRoomId(roomId).stream()
                .map(RoomDeviceStatusResponse::from)
                .toList();
    }
}
