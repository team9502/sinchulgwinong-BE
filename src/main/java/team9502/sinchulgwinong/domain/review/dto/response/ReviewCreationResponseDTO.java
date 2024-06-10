package team9502.sinchulgwinong.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreationResponseDTO {

    private Long reviewId;

    private Long cpUserId;

    private String reviewTitle;

    private String reviewContent;

    private Integer rating;
}
