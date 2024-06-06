package team9502.sinchulgwinong.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.user.enums.LoginType;

@Getter
@NoArgsConstructor
@Data
public class UserSignupRequestDTO {

    @NotBlank(message = "필수 입력입니다.")
    @Schema(description = "사용자 이름", example = "김은채")
    private String username;

    @NotBlank(message = "필수 입력입니다.")
    @Schema(description = "사용자 벌명", example = "대구총잡이")
    private String nickname;

    @Schema(description = "비밀번호", example = "password")
    @NotBlank(message = "필수 입력입니다.")
    private String password;

    @NotBlank(message = "필수 입력입니다.")
    @Schema(description = "비밀번호 확인", example = "password")
    private String confirmPassword;

    @NotBlank(message = "필수 입력입니다.")
    @Schema(description = "이메일 주소", example = "example@example.com")
    private String email;

    @Schema(description = "전화번호", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "사업자 번호", example = "1234567890")
    private String companyNum;

    @Schema(description = "로그인 유형", example = "EMAIL")
    private LoginType loginType;

    @Schema(description = "약관 동의 여부", example = "true")
    private boolean agreeToTerms;
}
