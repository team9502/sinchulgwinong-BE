package team9502.sinchulgwinong.domain.email.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationCodeRequestDTO {

    @Schema(description = "이메일 주소", example = "example@email.com")
    private String email;

    @Schema(description = "인증 코드", example = "123456")
    private String code;
}
