package team9502.sinchulgwinong.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    // Auth
    SUCCESS_USER_SIGN_UP(HttpStatus.CREATED, "구직자 회원 가입 성공"),
    SUCCESS_CP_USER_SIGN_UP(HttpStatus.CREATED, "기업 회원 가입 성공"),
    SUCCESS_USER_LOGIN(HttpStatus.OK, "구직자 로그인 성공"),
    SUCCESS_CP_USER_LOGIN(HttpStatus.OK, "기업 회원 로그인 성공"),

    // Global
    OK(HttpStatus.OK, "요청 성공");

    private final HttpStatus httpStatus;
    private final String message;
}
