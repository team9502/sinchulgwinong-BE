package team9502.sinchulgwinong.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    // Auth
    SUCCESS_USER_SIGN_UP(HttpStatus.CREATED, "회원 가입 성공"),

    // Global
    OK(HttpStatus.OK, "요청 성공");

    private final HttpStatus httpStatus;
    private final String message;
}
