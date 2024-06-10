package team9502.sinchulgwinong.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.review.entity.Review;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponseDTO {

    @Schema(description = "리뷰 목록")
    private List<ReviewResponseDTO> reviews;

    @Schema(description = "리뷰 개수")
    private int totalReviewCount;

    public ReviewListResponseDTO(List<Review> reviews) {
        this.reviews = reviews.stream()
                .map(ReviewResponseDTO::new)
                .toList();
        this.totalReviewCount = reviews.size();
    }
}