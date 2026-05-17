package ac.gachon.iot.dto;

import ac.gachon.iot.domain.enums.SensorStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorFindAllLatestResponse {

    private Long sensorId;

    private BigDecimal temperature;

    private BigDecimal humidity;

    private Short motion;

    private SensorStatus status;

}
