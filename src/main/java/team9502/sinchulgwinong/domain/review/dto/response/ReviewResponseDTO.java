package team9502.sinchulgwinong.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.review.entity.Review;

@Getter
@NoArgsConstructor
public class ReviewResponseDTO {

    @Schema(description = "리뷰 ID")
    private Long reviewId;

    @Schema(description = "리뷰 제목")
    private String reviewTitle;

    @Schema(description = "리뷰 내용")
    private String reviewContent;

    @Schema(description = "평점")
    private Integer rating;

    public ReviewResponseDTO(Review review) {
        this.reviewId = review.getReviewId();
        this.reviewTitle = review.getReviewTitle();
        this.reviewContent = review.getReviewContent();
        this.rating = review.getRating();
    }
}