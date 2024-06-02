package team9502.sinchulgwinong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // User & Auth
    REQUIRED_ADMIN_USER_AUTHORITY(HttpStatus.UNAUTHORIZED, "401", "관리자 권한이 필요합니다."),

    // Token
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "401", "유효하지 않은 JWT 토큰입니다."),

    // Global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 에러"),
    INTERNAL_BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "잘못된 요청입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "400", "유효성 검사 실패");


    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
