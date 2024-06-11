package team9502.sinchulgwinong.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.review.entity.Review;
import team9502.sinchulgwinong.domain.review.entity.UserReviewStatus;

@Getter
@NoArgsConstructor
public class UserReviewResponseDTO {

    @Schema(description = "리뷰 ID")
    private Long reviewId;

    @Schema(description = "리뷰 제목")
    private String reviewTitle;

    @Schema(description = "리뷰 내용")
    private String reviewContent;

    @Schema(description = "평점")
    private Integer rating;

    @Schema(description = "공개 여부")
    private Boolean isPrivate;

    public UserReviewResponseDTO(Review review, UserReviewStatus userReviewStatus) {
        this.reviewId = review.getReviewId();
        this.reviewTitle = review.getReviewTitle();
        this.reviewContent = review.getReviewContent();
        this.rating = review.getRating();
        this.isPrivate = userReviewStatus != null ? userReviewStatus.getIsPrivate() : true;
    }
}