package team9502.sinchulgwinong.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import team9502.sinchulgwinong.domain.review.entity.Review;
import team9502.sinchulgwinong.domain.review.entity.UserReviewStatus;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReviewListResponseDTO {

    @Schema(description = "리뷰 목록")
    private List<UserReviewResponseDTO> reviews;

    @Schema(description = "리뷰 개수")
    private int totalReviewCount;

    public UserReviewListResponseDTO(Page<Review> reviews, List<UserReviewStatus> statuses) {
        this.reviews = reviews.stream()
                .map(review -> {
                    UserReviewStatus status = statuses.stream()
                            .filter(s -> s.getReview().equals(review))
                            .findFirst()
                            .orElse(null);
                    return new UserReviewResponseDTO(review, status);
                })
                .toList();
        this.totalReviewCount = (int) reviews.getTotalElements();
    }
}