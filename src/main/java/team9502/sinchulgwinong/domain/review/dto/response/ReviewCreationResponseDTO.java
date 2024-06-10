package team9502.sinchulgwinong.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreationResponseDTO {

    @Schema(description = "생성된 리뷰의 ID", example = "101")
    private Long reviewId;

    @Schema(description = "리뷰 대상 기업 회원의 ID", example = "123")
    private Long cpUserId;

    @Schema(description = "리뷰 제목", example = "친절한 사장님!")
    private String reviewTitle;

    @Schema(description = "리뷰 내용", example = "사장님이 맛있고 사과가 친절해요.")
    private String reviewContent;

    @Schema(description = "리뷰 평점", example = "5")
    private Integer rating;
}
