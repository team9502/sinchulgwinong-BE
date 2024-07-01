package team9502.sinchulgwinong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //공통
    FORBIDDEN_WORK(HttpStatus.UNAUTHORIZED, "이 작업을 수행할 권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사 실패"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),

    // User & Auth
    REQUIRED_ADMIN_USER_AUTHORITY(HttpStatus.UNAUTHORIZED, "관리자 권한이 필요합니다."),
    LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, "로그인 실패"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    COMPANY_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "기업(회원)을 찾을 수 없습니다."),
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "중복된 이메일입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "입력한 비밀번호가 기존 비밀번호와 일치하지 않습니다."),
    TERMS_NOT_ACCEPTED(HttpStatus.BAD_REQUEST, "약관에 동의해야 합니다."),
    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "잘못된 로그인 유형입니다."),
    PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    PASSWORD_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요."),

    // Email
    EMAIL_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "이메일 인증에 실패했습니다."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
    EXPIRED_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "만료된 인증 코드입니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 코드입니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "이메일 인증이 필요합니다."),

    //chat
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅을 찾을 수 없습니다."),
    ALREADY_CREATE_CHAT(HttpStatus.NOT_FOUND, "이미 생성된 채팅방 입니다."),

    // Faq
    FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ를 찾을 수 없습니다."),

    //Board
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "제목은 100자를 초과할 수 없습니다."),
    CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "내용은 1000자를 초과할 수 없습니다."),

    //JobBoard
    JOB_BOARD_ALREADY_AD(HttpStatus.NOT_FOUND, "이미 구인 게시글 광고 중 입니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST,"지원하지 않는 확장자 입니다."),

    // Point
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "포인트를 찾을 수 없습니다."),
    INSUFFICIENT_POINTS(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),

    //Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    // Token
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "지원하지 않는 JWT 토큰 형식입니다."),
    MALFORMED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT 토큰 구조가 올바르지 않습니다."),
    SIGNATURE_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "JWT 서명 검증 실패."),
    NON_ILLEGAL_ARGUMENT_JWT_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 인자가 전달된 JWT입니다."),

    // Review
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 리뷰를 작성하셨습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    REVIEW_ALREADY_PUBLIC(HttpStatus.BAD_REQUEST, "이미 공개된 리뷰입니다."),

    // Global
    INTERNAL_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_USER_TYPE(HttpStatus.BAD_REQUEST, "잘못된 사용자 유형입니다.");


    private final HttpStatus httpStatus;

    private final String message;
}
