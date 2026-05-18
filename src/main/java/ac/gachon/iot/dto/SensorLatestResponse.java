package ac.gachon.iot.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SensorLatestResponse {
    private Long sensorId;
    private String sensorType;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private Short motion;
}
