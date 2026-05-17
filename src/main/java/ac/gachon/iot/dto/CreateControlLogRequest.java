package ac.gachon.iot.dto;

import lombok.Getter;

@Getter
public class CreateControlLogRequest {
    private String room_id;

    private String device_id;

    private String action;
}
