package ac.gachon.iot.dto;

import ac.gachon.iot.domain.entity.ControlLog;
import lombok.AllArgsConstructor;
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

    public static ControlLogResponse from(ControlLog controlLog) {
        return ControlLogResponse.builder()
                .roomName(controlLog.getRoom().getName())
                .deviceName(controlLog.getDevice().getName())
                .action(controlLog.getAction().name())
                .type(controlLog.getType().name())
                .createdAt(controlLog.getCreatedAt().toString())
                .build();

    }
}
