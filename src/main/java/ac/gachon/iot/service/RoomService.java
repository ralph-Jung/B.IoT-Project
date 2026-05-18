package ac.gachon.iot.service;

import ac.gachon.iot.domain.entity.Room;
import ac.gachon.iot.domain.entity.SensorData;
import ac.gachon.iot.domain.repository.RoomRepository;
import ac.gachon.iot.domain.repository.SensorDataRepository;
import ac.gachon.iot.dto.AllRoomsResponse;
import ac.gachon.iot.dto.DeviceStatusResponse;
import ac.gachon.iot.dto.RoomSensorLatestResponse;
import ac.gachon.iot.dto.SensorLatestResponse;
import ac.gachon.iot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final SensorDataRepository sensorDataRepository;

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


    public RoomSensorLatestResponse findLatestSensorsByRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 방입니다. id=" + roomId));

        List<SensorData> sensorDataByRoom = sensorDataRepository.findLatestsSensorsByRoom(roomId);

        return RoomSensorLatestResponse.builder()
                .roomId(roomId)
                .roomName(room.getName())
                .sensors(sensorDataByRoom.stream().map(sensorData -> SensorLatestResponse.builder()
                                .sensorId(sensorData.getSensor().getId())
                                .sensorType(sensorData.getSensor().getSensorType().getName())
                                .humidity(sensorData.getHumidity())
                                .motion(sensorData.getMotion())
                                .temperature(sensorData.getTemperature())
                                .build())
                        .toList())
                .build();
    }

}
