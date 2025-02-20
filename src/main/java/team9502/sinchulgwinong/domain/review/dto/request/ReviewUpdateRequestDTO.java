package team9502.sinchulgwinong.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewUpdateRequestDTO {

    @NotBlank(message = "리뷰 제목은 필수입니다.")
    private String reviewTitle;

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String reviewContent;

    @NotNull(message = "평점은 필수입니다.")
    private Integer rating;
}
