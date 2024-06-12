package team9502.sinchulgwinong.domain.point.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointSummaryResponseDTO {

    @Schema(description = "적립된 포인트")
    private int totalSaved;

    @Schema(description = "사용한 포인트")
    private int totalUsed;
}
