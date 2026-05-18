package ac.gachon.iot.service;

import ac.gachon.iot.domain.entity.Device;
import ac.gachon.iot.domain.entity.Room;
import ac.gachon.iot.domain.entity.RoomDevice;
import ac.gachon.iot.domain.repository.RoomRepository;
import ac.gachon.iot.dto.AllRoomsResponse;
import ac.gachon.iot.dto.DeviceStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<AllRoomsResponse> findAllRooms() {
        
        List<Room> allRooms = roomRepository.findAllWithDevices();

        return allRooms.stream().map(room -> AllRoomsResponse.builder()
                .roomId(room.getId())
                .roomName(room.getName())
                .devices(room.getRoomDevices().stream()
                        .map(rd -> DeviceStatusResponse.builder()
                                .deviceId(rd.getDevice().getId())
                                .deviceType(rd.getDevice().getName())
                                .status(rd.getStatus().name())
                                .build())
                        .toList())
                .build()
        ).toList();

    }

}
