package ac.gachon.iot.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RoomSensorLatestResponse {
    private Long roomId;
    private String roomName;
    private List<SensorLatestResponse> sensors;
}
