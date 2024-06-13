package team9502.sinchulgwinong.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequestDTO {

    @Schema(description = "사용자명", example = "김수정")
    private String username;

    @Schema(description = "닉네임", example = "수정이의 별명")
    private String nickname;

    @Schema(description = "이메일", example = "fix@email.com")
    private String email;

    @Schema(description = "전화번호", example = "010-5678-1234")
    private String phoneNumber;
}
