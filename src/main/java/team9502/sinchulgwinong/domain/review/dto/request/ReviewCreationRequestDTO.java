package team9502.sinchulgwinong.domain.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreationRequestDTO {

    private Long cpUserId;

    private String reviewTitle;

    private String reviewContent;

    private Integer rating;
}
