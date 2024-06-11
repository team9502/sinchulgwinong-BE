package team9502.sinchulgwinong.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreationRequestDTO {

    @Schema(description = "리뷰 대상 기업 회원의 ID", example = "123")
    @NotNull(message = "기업 회원 ID는 필수입니다.")
    private Long cpUserId;

    @Schema(description = "리뷰 제목", example = "친절한 사장님!")
    @NotNull(message = "리뷰 제목은 필수입니다.")
    @Size(max = 100, message = "리뷰 제목은 100자를 초과할 수 없습니다.")
    private String reviewTitle;

    @Schema(description = "리뷰 내용", example = "사장님이 맛있고 사과가 친절해요.")
    @NotNull(message = "리뷰 내용은 필수입니다.")
    @Size(max = 1000, message = "리뷰 내용은 1000자를 초과할 수 없습니다.")
    private String reviewContent;

    @Schema(description = "리뷰 평점", example = "5")
    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5점이어야 합니다.")
    private Integer rating;
}
