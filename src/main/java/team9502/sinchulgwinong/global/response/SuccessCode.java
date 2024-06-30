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
    SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃 성공"),

    // User
    SUCCESS_USER_PROFILE_READ(HttpStatus.OK, "구직자 프로필 조회 성공"),
    SUCCESS_USER_PROFILE_UPDATED(HttpStatus.OK, "구직자 프로필 수정 성공"),
    SUCCESS_USER_PASSWORD_UPDATED(HttpStatus.OK, "구직자 비밀번호 수정 성공"),
    SUCCESS_USER_DELETED(HttpStatus.OK, "구직자 회원 탈퇴 성공"),

    // SocialLogin
    SUCCESS_SOCIAL_LOGIN(HttpStatus.OK, "소셜 로그인 성공"),

    // Email
    SUCCESS_EMAIL_VERIFICATION_SENT(HttpStatus.OK, "이메일 인증 코드 발송 성공"),
    SUCCESS_EMAIL_VERIFICATION(HttpStatus.OK, "이메일 인증 성공"),

    // Faq
    SUCCESS_FAQ_CREATE(HttpStatus.CREATED, "FAQ 생성 성공"),
    SUCCESS_FAQ_LIST_READ(HttpStatus.OK, "FAQ 전체 조회 성공"),
    SUCCESS_FAQ_READ(HttpStatus.OK, "FAQ 단건 조회 성공"),
    SUCCESS_FAQ_UPDATE(HttpStatus.OK, "FAQ 수정 성공"),
    SUCCESS_FAQ_DELETE(HttpStatus.OK, "FAQ 삭제 성공"),

    // CompanyUser
    SUCCESS_CP_USER_PROFILE_READ(HttpStatus.OK, "기업(회원) 프로필 조회 성공"),
    SUCCESS_CP_USER_DETAIL_READ(HttpStatus.OK, "기업(회원) 상세 조회 성공"),
    SUCCESS_CP_USER_PROFILE_UPDATED(HttpStatus.OK, "기업(회원) 프로필 수정 성공"),
    SUCCESS_CP_USER_PASSWORD_UPDATED(HttpStatus.OK, "기업(회원) 비밀번호 수정 성공"),
    SUCCESS_CP_USER_ALL_READ(HttpStatus.OK, "기업(회원) 전체 조회 성공"),
    SUCCESS_CP_USER_DELETED(HttpStatus.OK, "기업(회원) 회원 탈퇴 성공"),

    //Chat
    SUCCESS_CREATE_CHAT_ROOM(HttpStatus.CREATED, "채팅방 생성 성공"),
    SUCCESS_SEND_AND_SAVE_CHAT(HttpStatus.CREATED, "메세지 보내기, 저장 성공"),
    SUCCESS_READ_CHAT_MESSAGES(HttpStatus.OK, "메세지 내역 조회 성공"),
    SUCCESS_READ_CHAT_ROOM(HttpStatus.OK, "채팅방 조회 성공"),

    //Board
    SUCCESS_CREATE_BOARD(HttpStatus.CREATED, "게시글 생성 성공"),
    SUCCESS_READ_ALL_BOARD(HttpStatus.OK, "게시글 전체 조회 성공"),
    SUCCESS_READ_BOARD(HttpStatus.OK, "게시글 단건 조회 성공"),
    SUCCESS_UPDATE_BOARD(HttpStatus.OK, "게시글 업데이트 성공"),
    SUCCESS_DELETE_BOARD(HttpStatus.OK, "게시글 삭제 성공"),
    SUCCESS_READ_ALL_MY_BOARD(HttpStatus.OK, "내가 작성한 게시글 전체 조회 성공"),
    SUCCESS_READ_FIND_BOARD(HttpStatus.OK, "검색어로 게시글 전체 조회 성공"),

    // Point
    SUCCESS_POINT_SUMMARY_READ(HttpStatus.OK, "포인트 총액 조회 성공"),
    SUCCESS_SAVED_POINT_READ(HttpStatus.OK, "적립 포인트 조회 성공"),
    SUCCESS_USED_POINT_READ(HttpStatus.OK, "사용 포인트 조회 성공"),
    SUCCESS_BANNER_POINT_READ(HttpStatus.OK, "배너 포인트 TOP3 조회 성공"),
    SUCCESS_POINTS_USED_FOR_BANNER(HttpStatus.OK, "배너 포인트 사용 성공"),

    //JobBoard
    SUCCESS_CREATE_JOB_BOARD(HttpStatus.CREATED, "구인게시글 생성 성공"),
    SUCCESS_READ_ALL_JOB_BOARD(HttpStatus.OK, "구인게시글 전체 조회 성공"),
    SUCCESS_READ_JOB_BOARD(HttpStatus.OK, "구인게시글 단건 조회 성공"),
    SUCCESS_UPDATE_JOB_BOARD(HttpStatus.OK, "구인게시글 업데이트 성공"),
    SUCCESS_DELETE_JOB_BOARD(HttpStatus.OK, "구인게시글 삭제 성공"),
    SUCCESS_READ_ALL_MY_JOB_BOARD(HttpStatus.OK, "내가 작성한 게시글 전체 조회 성공"),
    SUCCESS_AD_JOB_BOARD(HttpStatus.CREATED, "구인게시글 광고 성공"),
    SUCCESS_READ_AD_JOB_BOARD(HttpStatus.OK, "광고된 구인게시글 전체 조회 성공"),
    SUCCESS_SAVE_OPEN_API(HttpStatus.OK, "openApi 저장 성공"),
    SUCCESS_READ_CATEGORY_JOB_BOARD(HttpStatus.OK, "구인게시글 카테고리 조회 성공"),
    SUCCESS_READ_ALL_CATEGORY_JOB_BOARD(HttpStatus.OK, "구인게시글 카테고리 검색 성공"),

    // Review
    SUCCESS_REVIEW_CREATION(HttpStatus.CREATED, "리뷰 작성 성공"),
    SUCCESS_CP_USER_REVIEW_READ(HttpStatus.OK, "기업 리뷰 전체 조회 성공"),
    SUCCESS_ALL_REVIEW_READ(HttpStatus.OK, "리뷰 전체 조회 성공"),
    SUCCESS_USER_USE_POINT_TO_REVIEW(HttpStatus.OK, "포인트 사용하여 리뷰 조회 성공"),
    SUCCESS_REVIEW_VISIBILITY_REQUEST(HttpStatus.CREATED, "리뷰 게시 중단 요청 성공"),

    //Comment
    SUCCESS_CREATE_COMMENT(HttpStatus.CREATED, "댓글 생성 성공"),
    SUCCESS_READ_ALL_COMMENT(HttpStatus.OK, "댓글 전체 조회 성공"),
    SUCCESS_UPDATE_COMMENT(HttpStatus.OK, "댓글 업데이트 성공"),
    SUCCESS_DELETE_COMMENT(HttpStatus.OK, "댓글 삭제 성공"),
    SUCCESS_READ_ALL_MY_COMMENT(HttpStatus.OK, "내가 작성한 댓글 전체 조회 성공"),

    //Scrap
    SUCCESS_CREATE_SCRAP(HttpStatus.CREATED, "스크랩 생성 성공"),
    SUCCESS_READ_ALL_SCRAP(HttpStatus.OK, "스크랩 전체 조회 성공"),
    SUCCESS_DELETE_SCRAP(HttpStatus.OK, "스크랩 삭제 성공"),

    // Global
    OK(HttpStatus.OK, "요청 성공");

    private final HttpStatus httpStatus;
    private final String message;
}
