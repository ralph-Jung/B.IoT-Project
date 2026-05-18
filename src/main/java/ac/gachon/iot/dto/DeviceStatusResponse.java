package ac.gachon.iot.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class DeviceStatusResponse {

    private Long deviceId;
    private String deviceType;
    private String status;
}
