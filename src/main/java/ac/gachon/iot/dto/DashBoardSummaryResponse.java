package ac.gachon.iot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashBoardSummaryResponse {

    private Integer totalSenorCount;

    private Integer totalAlertCount;

    private Long currentTotalWatt;

    private Long efficiency;
}
