package team9502.sinchulgwinong.domain.point.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.point.enums.SpType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SavedPointDetailResponseDTO {

    @Schema(description = "적립된 포인트", example = "REVIEW")
    private SpType type;

    @Schema(description = "적립된 포인트", example = "100")
    private Integer savedPoint;

    @Schema(description = "적립된 날짜", example = "2021-07-01")
    private LocalDate createdAt;
}
