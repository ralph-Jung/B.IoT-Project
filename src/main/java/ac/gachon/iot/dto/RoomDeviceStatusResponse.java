package ac.gachon.iot.dto;

import ac.gachon.iot.domain.entity.RoomDevice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomDeviceStatusResponse {
    private String deviceName;
    private String status;

    public static RoomDeviceStatusResponse from(RoomDevice rd) {
        return RoomDeviceStatusResponse.builder()
                .deviceName(rd.getDevice().getName())
                .status(rd.getStatus().name())
                .build();
    }
}
