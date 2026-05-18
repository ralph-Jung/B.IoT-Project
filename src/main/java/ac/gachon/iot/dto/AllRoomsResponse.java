package ac.gachon.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class AllRoomsResponse {
    private Long roomId;
    private String roomName;
    private List<DeviceStatusResponse> devices;
}
