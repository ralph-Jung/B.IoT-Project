package ac.gachon.iot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnergyEfficiencyResponse {
    private Long efficiency;
}
