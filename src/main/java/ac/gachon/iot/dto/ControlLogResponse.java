package ac.gachon.iot.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@Getter
public class ControlLogResponse {

    private String roomName;

    private String deviceName;

    private String action;

    private String type;

    private String createdAt;
}
