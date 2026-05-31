package ac.gachon.iot.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
public class SensorDataMessage {
    private Long sensorId;
    private Long roomId;
    private String sensorIdentifier;
    private String sensorType;      // SensorType.name
    private BigDecimal temperature;
    private BigDecimal humidity;
    private Short motion;
    private String status;          // "NORMAL" | "ANOMALY"
    private OffsetDateTime createdAt;
}
