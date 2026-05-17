package ac.gachon.iot.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthLoginResponse {
    private String token;
}
