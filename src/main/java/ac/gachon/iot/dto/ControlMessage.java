package ac.gachon.iot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ControlMessage {
    private Long roomId;
    private String deviceName;
    private String action;
    private String type;
}
