package team9502.sinchulgwinong.domain.oauth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequestDTO {

    @NotBlank(message = "코드를 입력해주세요.")
    @Schema(description = "구글에서 제공받은 코드", example = "4/0AX4XfW....")
    private String code;

    @NotBlank(message = "이름을 입력해주세요.")
    @Schema(description = "사용자 이름", example = "김은채")
    private String username;

    @Schema(description = "사용자 별명", example = "대구총잡이")
    private String nickname;

    @Schema(description = "전화번호", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "약관 동의 여부", example = "true")
    private boolean agreeToTerms;

    @NotBlank(message = "로그인 타입을 입력해주세요.")
    @Schema(description = "로그인 타입", example = "GOOGLE")
    private String loginType;
}
