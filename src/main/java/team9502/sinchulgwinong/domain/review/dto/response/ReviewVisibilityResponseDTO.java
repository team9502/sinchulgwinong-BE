package team9502.sinchulgwinong.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewVisibilityResponseDTO {

    @Schema(description = "게시 중단을 하려고 하는 리뷰 ID", example = "1")
    private Long requestId;

    @Schema(description = "요청한 기업 회원 ID", example = "5001")
    private Long cpUserId;

    @Schema(description = "게시 중단을 요청하는 기업 이메일", example = "email@email.com")
    private String cpEmail;

    @Schema(description = "요청 처리 시간", example = "2024-06-01T15:21:14")
    private LocalDateTime requestTime;
}
