package team9502.sinchulgwinong.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewVisibilityRequestDTO {

    @Schema(description = "게시 중단을 하려고 하는 리뷰 ID", example = "1")
    private Long reviewId;

    @Schema(description = "게시 중단을 요청하는 기업 이메일", example = "email@email.com")
    private String cpEmail;

    @Schema(description = "게시 중단을 요청하는 기업의 사업자등록번호", example = "1234567890")
    private String cpNum;

    @Schema(description = "게시 중단을 요청하는 이유", example = "사실과 다른 내용이 포함되어 있음")
    private String reason;
}
