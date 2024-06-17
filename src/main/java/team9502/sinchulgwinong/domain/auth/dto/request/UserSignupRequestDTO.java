package team9502.sinchulgwinong.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.user.enums.LoginType;

@Getter
@NoArgsConstructor
@Data
public class UserSignupRequestDTO {

    @NotBlank(message = "이름을 입력해주세요.")
    @Schema(description = "사용자 이름", example = "김은채")
    private String username;

    @Schema(description = "사용자 벌명", example = "대구총잡이")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            , message = "비밀번호는 최소 8자리이며, 알파벳, 숫자, 특수문자를 포함해야 합니다.")
    @Schema(description = "비밀번호", example = "password")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    @Schema(description = "비밀번호 확인", example = "password")
    private String confirmPassword;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z]{2,})+$", message = "유효한 이메일 주소를 입력해주세요.")
    @Schema(description = "이메일 주소", example = "example@example.com")
    private String email;

    @Schema(description = "전화번호", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "로그인 유형", example = "EMAIL")
    private LoginType loginType;

    @Schema(description = "약관 동의 여부", example = "true")
    private boolean agreeToTerms;
}
