package ac.gachon.iot.dto;

import ac.gachon.iot.domain.enums.AlertType;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;

@ToString
@Getter
public class AlertLogResponse {

    private Long id;
    private AlertType type;
    private String message;
    private OffsetDateTime createdAt;
    private String roomName;

    public AlertLogResponse(Long id, AlertType type, String message, OffsetDateTime createdAt, String roomName) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
        this.roomName = roomName;
    }
}
