package team9502.sinchulgwinong.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.oauth.enums.SocialType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO {

    private Long userId;

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private SocialType loginType;
}
