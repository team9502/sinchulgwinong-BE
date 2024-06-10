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

    //Board
    SUCCESS_CREATE_BOARD(HttpStatus.CREATED, "게시글 생성 성공"),
    SUCCESS_READ_ALL_BOARD(HttpStatus.OK, "게시글 전체 조회 성공"),
    SUCCESS_READ_BOARD(HttpStatus.OK, "게시글 단건 조회 성공"),
    SUCCESS_UPDATE_BOARD(HttpStatus.OK, "게시글 업데이트 성공"),
    SUCCESS_DELETE_BOARD(HttpStatus.OK, "게시글 삭제 성공"),

    // Review
    SUCCESS_REVIEW_CREATION(HttpStatus.CREATED, "리뷰 작성 성공"),

    //Comment
    SUCCESS_CREATE_COMMENT(HttpStatus.CREATED, "댓글 생성 성공"),
    SUCCESS_READ_ALL_COMMENT(HttpStatus.OK, "댓글 전체 조회 성공"),
    SUCCESS_UPDATE_COMMENT(HttpStatus.OK, "댓글 업데이트 성공"),
    SUCCESS_DELETE_COMMENT(HttpStatus.OK, "댓글 삭제 성공"),

    //Scrap
    SUCCESS_CREATE_SCRAP(HttpStatus.CREATED, "스크랩 생성 성공"),
    SUCCESS_READ_ALL_SCRAP(HttpStatus.OK, "스크랩 전체 조회 성공"),
    SUCCESS_DELETE_SCRAP(HttpStatus.OK, "스크랩 삭제 성공"),

    // Global
    OK(HttpStatus.OK, "요청 성공");

    private final HttpStatus httpStatus;
    private final String message;
}
