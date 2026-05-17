package ac.gachon.iot.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Builder
@Getter
public class EnergyDailyResponse {

    private OffsetDateTime date;

    private Double totalWh;

}
