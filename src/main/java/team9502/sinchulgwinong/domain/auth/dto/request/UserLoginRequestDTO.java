package team9502.sinchulgwinong.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginRequestDTO {

    @Schema(description = "이메일", example = "example@email.com")
    @NotBlank(message = "필수 입력입니다.")
    private String email;

    @Schema(description = "비밀번호", example = "Password1!")
    @NotBlank(message = "필수 입력입니다.")
    private String password;

    @Builder
    public UserLoginRequestDTO(String email, String password) {

        this.email = email;
        this.password = password;
    }
}
