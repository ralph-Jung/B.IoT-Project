package ac.gachon.iot.controller;

import ac.gachon.iot.dto.AllRoomsResponse;
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

    @GetMapping("")
    public List<AllRoomsResponse> getAllRooms() {
        return roomService.findAllRooms();
    }

    @GetMapping("{roomId}/sensors/latest")
    public RoomSensorLatestResponse getLatestSensorsByRoom(@Parameter(description = "방 ID", required = true)
                                                           @PathVariable Long roomId) {
        return roomService.findLatestSensorsByRoom(roomId);


    }
}
