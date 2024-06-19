package team9502.sinchulgwinong.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.oauth.enums.SocialType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDTO {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사용자 이름", example = "김은채")
    private String username;

    @Schema(description = "사용자 닉네임", example = "정신체리라")
    private String nickname;

    @Schema(description = "사용자 이메일", example = "example@email.com")
    private String email;

    @Schema(description = "사용자 전화번호", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "로그인 타입", example = "KAKAO")
    private SocialType loginType;
}
