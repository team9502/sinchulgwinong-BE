package team9502.sinchulgwinong.domain.companyUser.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CpUserPasswordUpdateRequestDTO {

    @Schema(description = "현재 비밀번호", example = "Password1!")
    private String currentPassword;

    @Schema(description = "변경할 비밀번호", example = "newPassword1!")
    private String newPassword;

    @Schema(description = "변경할 비밀번호 확인", example = "newPassword1!")
    private String newPasswordConfirm;
}
