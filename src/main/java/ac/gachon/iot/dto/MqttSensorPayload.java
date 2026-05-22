package ac.gachon.iot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MqttSensorPayload(
        BigDecimal temperature,
        BigDecimal humidity,
        Short motion
) {}
