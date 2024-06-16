package team9502.sinchulgwinong.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import team9502.sinchulgwinong.domain.review.entity.Review;
import team9502.sinchulgwinong.domain.review.entity.UserReviewStatus;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReviewListResponseDTO {

    @Schema(description = "리뷰 목록")
    private List<UserReviewResponseDTO> reviews;

    @Schema(description = "리뷰 개수")
    private int totalReviewCount;

    @Schema(description = "현재 페이지 번호")
    private int currentPage;

    @Schema(description = "전체 페이지 수")
    private int totalPages;

    public UserReviewListResponseDTO(Page<Review> reviews, List<UserReviewStatus> statuses) {

        this.reviews = reviews.getContent().stream()
                .map(review -> {
                    UserReviewStatus status = statuses.stream()
                            .filter(s -> s.getReview().equals(review))
                            .findFirst()
                            .orElse(null);
                    return new UserReviewResponseDTO(review, status);
                })
                .collect(Collectors.toList());
        this.totalReviewCount = (int) reviews.getTotalElements();
        this.currentPage = reviews.getNumber();
        this.totalPages = reviews.getTotalPages();
    }
}