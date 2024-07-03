package team9502.sinchulgwinong.domain.point.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.point.enums.UpType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsedPointDetailResponseDTO {

    @Schema(description = "사용된 포인트 ID", example = "1")
    private Long upId;

    @Schema(description = "사용된 포인트", example = "REVIEW")
    private UpType type;

    @Schema(description = "사용된 포인트", example = "100")
    private Integer usedPoint;

    @Schema(description = "사용된 날짜", example = "2021-07-01")
    private LocalDate usedAt;
}
