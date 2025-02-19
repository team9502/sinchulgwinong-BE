package team9502.sinchulgwinong.domain.review.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.enums.UpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.domain.review.dto.request.ReviewCreationRequestDTO;
import team9502.sinchulgwinong.domain.review.dto.response.ReviewCreationResponseDTO;
import team9502.sinchulgwinong.domain.review.dto.response.ReviewListResponseDTO;
import team9502.sinchulgwinong.domain.review.dto.response.ReviewResponseDTO;
import team9502.sinchulgwinong.domain.review.dto.response.UserReviewListResponseDTO;
import team9502.sinchulgwinong.domain.review.entity.Review;
import team9502.sinchulgwinong.domain.review.entity.UserReviewStatus;
import team9502.sinchulgwinong.domain.review.enums.ReviewStatus;
import team9502.sinchulgwinong.domain.review.enums.ReviewerResponse;
import team9502.sinchulgwinong.domain.review.repository.ReviewRepository;
import team9502.sinchulgwinong.domain.review.repository.ReviewVisibilityRequestsRepository;
import team9502.sinchulgwinong.domain.review.repository.UserReviewStatusRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceTest.class);

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CompanyUserRepository companyUserRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserReviewStatusRepository userReviewStatusRepository;
    @Mock
    private PointService pointService;
    @Mock
    private ReviewVisibilityRequestsRepository reviewVisibilityRequestsRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User mockUser;
    private CompanyUser mockCompanyUser;
    private Review mockReview;

    @BeforeEach
    void setUp() {
        // 공통으로 사용하는 Mock 객체들 초기화
        mockUser = new User();
        mockUser.setUserId(10L);
        mockUser.setEmail("test@example.com");

        mockCompanyUser = CompanyUser.builder()
                                     .cpUserId(100L)
                                     .cpName("TestCompany")
                                     .reviewCount(0)
                                     .averageRating(0.0f)
                                     .build();

        mockReview = Review.builder()
                           .reviewId(1000L)
                           .user(mockUser)
                           .cpUser(mockCompanyUser)
                           .reviewTitle("테스트 리뷰 제목")
                           .reviewContent("테스트 리뷰 내용")
                           .rating(5)
                           .status(ReviewStatus.ACTIVE)
                           .build();
    }

    @Nested
    @DisplayName("리뷰 생성 테스트")
    class CreateReviewTest {

        @Test
        @DisplayName("리뷰 생성 성공")
        void 리뷰생성_성공() {
            logger.info("===== 리뷰생성_성공 테스트 시작 =====");

            // given
            ReviewCreationRequestDTO requestDTO = new ReviewCreationRequestDTO();
            requestDTO.setCpUserId(100L);
            requestDTO.setReviewTitle("새 리뷰");
            requestDTO.setReviewContent("새 리뷰 내용");
            requestDTO.setRating(4);

            when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
            when(companyUserRepository.findById(100L)).thenReturn(Optional.of(mockCompanyUser));
            when(reviewRepository.existsByUserAndCpUser(mockUser, mockCompanyUser)).thenReturn(false);

            // review save 시점에 빌더로 새 Review를 생성해서 반환
            UserReviewStatus status = UserReviewStatus.builder()
                                                      .user(mockUser)
                                                      .review(mockReview)
                                                      .isPrivate(false)
                                                      .build();
            when(userReviewStatusRepository.findByUserAndReview(mockUser, mockReview))
                .thenReturn(Optional.of(status));


            // when
            ReviewCreationResponseDTO response = reviewService.createReview(10L, requestDTO);

            // then
            assertNotNull(response);
            assertEquals(1234L, response.getReviewId()); // mockReviewId
            assertEquals(100L, response.getCpUserId());
            assertEquals("새 리뷰", response.getReviewTitle());
            verify(reviewRepository).existsByUserAndCpUser(mockUser, mockCompanyUser);
            verify(reviewRepository).save(any(Review.class));
            verify(userReviewStatusRepository).save(any(UserReviewStatus.class));
            verify(pointService).earnPoints(mockUser, SpType.REVIEW);
        }

        @Test
        @DisplayName("리뷰 생성 실패: 사용자 없음")
        void 리뷰생성_실패_사용자없음() {
            logger.warn("===== 리뷰생성_실패_사용자없음 테스트 시작 =====");

            // given
            when(userRepository.findById(10L)).thenReturn(Optional.empty());

            ReviewCreationRequestDTO requestDTO = new ReviewCreationRequestDTO();
            requestDTO.setCpUserId(100L);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                reviewService.createReview(10L, requestDTO);
            });
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(reviewRepository, never()).save(any(Review.class));
        }

        @Test
        @DisplayName("리뷰 생성 실패: 회사 없음")
        void 리뷰생성_실패_회사없음() {
            logger.warn("===== 리뷰생성_실패_회사없음 테스트 시작 =====");

            // given
            when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
            when(companyUserRepository.findById(999L)).thenReturn(Optional.empty());

            ReviewCreationRequestDTO requestDTO = new ReviewCreationRequestDTO();
            requestDTO.setCpUserId(999L);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                reviewService.createReview(10L, requestDTO);
            });
            assertEquals(ErrorCode.COMPANY_USER_NOT_FOUND, exception.getErrorCode());
            verify(reviewRepository, never()).save(any(Review.class));
        }

        @Test
        @DisplayName("리뷰 생성 실패: 이미 작성된 리뷰")
        void 리뷰생성_실패_이미존재하는리뷰() {
            logger.warn("===== 리뷰생성_실패_이미존재하는리뷰 테스트 시작 =====");

            // given
            when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
            when(companyUserRepository.findById(100L)).thenReturn(Optional.of(mockCompanyUser));
            when(reviewRepository.existsByUserAndCpUser(mockUser, mockCompanyUser)).thenReturn(true);

            ReviewCreationRequestDTO requestDTO = new ReviewCreationRequestDTO();
            requestDTO.setCpUserId(100L);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                reviewService.createReview(10L, requestDTO);
            });
            assertEquals(ErrorCode.REVIEW_ALREADY_EXISTS, exception.getErrorCode());
            verify(reviewRepository, never()).save(any(Review.class));
        }
    }

    @Nested
    @DisplayName("기업 리뷰 목록 조회 테스트")
    class FindAllReviewsByCompanyUserIdTest {

        @Test
        @DisplayName("기업 리뷰 목록 조회 성공")
        void 리뷰목록조회_성공() {
            logger.info("===== 리뷰목록조회_성공 테스트 시작 =====");

            // given
            List<Review> reviews = new ArrayList<>();
            reviews.add(mockReview);
            when(reviewRepository.findByCpUser_CpUserIdAndStatus(100L, ReviewStatus.ACTIVE)).thenReturn(reviews);

            // when
            ReviewListResponseDTO response = reviewService.findAllReviewsByCompanyUserId(100L);

            // then
            assertNotNull(response);
            assertFalse(response.getReviews().isEmpty());
            assertEquals(1, response.getReviews().size());
            verify(reviewRepository).findByCpUser_CpUserIdAndStatus(100L, ReviewStatus.ACTIVE);
        }
    }

    @Nested
    @DisplayName("사용자 리뷰 & 공개 여부 조회 테스트")
    class GetReviewsWithVisibilityTest {

        @Mock
        private Page<Review> reviewPage;

        @Test
        @DisplayName("사용자 리뷰 목록+가시성 조회 성공")
        void 리뷰가시성조회_성공() {
            logger.info("===== 리뷰가시성조회_성공 테스트 시작 =====");

            // given
            User user = new User();
            user.setUserId(10L);

            List<Review> reviewList = List.of(mockReview);
            when(reviewPage.getContent()).thenReturn(reviewList);

            when(userRepository.findById(10L)).thenReturn(Optional.of(user));
            when(reviewRepository.findReviewsByCpUser_CpUserIdAndStatus(eq(100L), eq(ReviewStatus.ACTIVE), any(Pageable.class)))
                .thenReturn(reviewPage);

            List<UserReviewStatus> statusList = new ArrayList<>();
            UserReviewStatus status = UserReviewStatus.builder()
                                                      .user(user)
                                                      .review(mockReview)
                                                      .isPrivate(false)
                                                      .build();
            statusList.add(status);

            when(userReviewStatusRepository.findByUserAndReviewIn(eq(user), eq(reviewList))).thenReturn(statusList);

            // when
            UserReviewListResponseDTO response = reviewService.getReviewsWithVisibility(100L, 10L, 0, 10);

            // then
            assertNotNull(response);
            verify(userRepository).findById(10L);
            verify(reviewRepository).findReviewsByCpUser_CpUserIdAndStatus(eq(100L), eq(ReviewStatus.ACTIVE), any(Pageable.class));
            verify(userReviewStatusRepository).findByUserAndReviewIn(eq(user), anyList());
        }

        @Test
        @DisplayName("사용자 리뷰 목록+가시성 조회 실패: 사용자 없음")
        void 리뷰가시성조회_실패_사용자없음() {
            logger.warn("===== 리뷰가시성조회_실패_사용자없음 테스트 시작 =====");

            // given
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                reviewService.getReviewsWithVisibility(100L, 999L, 0, 10);
            });
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(reviewRepository, never()).findReviewsByCpUser_CpUserIdAndStatus(anyLong(), any(), any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("리뷰 상세 보기(가시성 전환) 테스트")
    class ViewReviewTest {

        @Test
        @DisplayName("리뷰 보기 성공: 최초 열람 (UserReviewStatus가 없음)")
        void 리뷰보기_성공_최초열람() {
            logger.info("===== 리뷰보기_성공_최초열람 테스트 시작 =====");

            // given
            when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
            when(reviewRepository.findByReviewIdAndStatus(1000L, ReviewStatus.ACTIVE)).thenReturn(Optional.of(mockReview));
            when(userReviewStatusRepository.findByUserAndReview(mockUser, mockReview)).thenReturn(null);

            // when
            ReviewResponseDTO response = reviewService.viewReview(1000L, 10L);

            // then
            assertNotNull(response);
            verify(pointService).deductPoints(mockUser, UpType.REVIEW);
            verify(userReviewStatusRepository).save(any(UserReviewStatus.class));
        }

        @Test
        @DisplayName("리뷰 보기 성공: 비공개 상태였다가 공개 처리")
        void 리뷰보기_성공_비공개에서공개() {
            logger.info("===== 리뷰보기_성공_비공개에서공개 테스트 시작 =====");

            // given
            when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
            when(reviewRepository.findByReviewIdAndStatus(1000L, ReviewStatus.ACTIVE)).thenReturn(Optional.of(mockReview));

            UserReviewStatus status = UserReviewStatus.builder()
                                                      .user(mockUser)
                                                      .review(mockReview)
                                                      .isPrivate(true) // 비공개 상태
                                                      .build();

            when(userReviewStatusRepository.findByUserAndReview(mockUser, mockReview))
                .thenReturn(Optional.of(status));

            // when
            ReviewResponseDTO response = reviewService.viewReview(1000L, 10L);

            // then
            assertNotNull(response);
            // 포인트 차감 + 상태 공개
            verify(pointService).deductPoints(mockUser, UpType.REVIEW);
            verify(userReviewStatusRepository).save(any(UserReviewStatus.class));
            assertFalse(status.getIsPrivate());
        }

        @Test
        @DisplayName("리뷰 보기 실패: 사용자 없음")
        void 리뷰보기_실패_사용자없음() {
            logger.warn("===== 리뷰보기_실패_사용자없음 테스트 시작 =====");

            // given
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                reviewService.viewReview(1000L, 999L);
            });
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(pointService, never()).deductPoints(any(User.class), any(UpType.class));
        }

        @Test
        @DisplayName("리뷰 보기 실패: 리뷰 없음")
        void 리뷰보기_실패_리뷰없음() {
            logger.warn("===== 리뷰보기_실패_리뷰없음 테스트 시작 =====");

            // given
            when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
            when(reviewRepository.findByReviewIdAndStatus(9999L, ReviewStatus.ACTIVE)).thenReturn(Optional.empty());

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                reviewService.viewReview(9999L, 10L);
            });
            assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("리뷰 보기 실패: 이미 공개 상태")
        void 리뷰보기_실패_이미공개상태() {
            logger.warn("===== 리뷰보기_실패_이미공개상태 테스트 시작 =====");

            // given
            when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
            when(reviewRepository.findByReviewIdAndStatus(1000L, ReviewStatus.ACTIVE)).thenReturn(Optional.of(mockReview));

            UserReviewStatus status = UserReviewStatus.builder()
                                                      .user(mockUser)
                                                      .review(mockReview)
                                                      .isPrivate(false) // 이미 공개 상태
                                                      .build();
            when(userReviewStatusRepository.findByUserAndReview(mockUser, mockReview))
                .thenReturn(Optional.of(status));


            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                reviewService.viewReview(1000L, 10L);
            });
            assertEquals(ErrorCode.REVIEW_ALREADY_PUBLIC, exception.getErrorCode());
            verify(pointService, never()).deductPoints(any(User.class), any(UpType.class));
        }
    }

    @Nested
    @DisplayName("리뷰 삭제 테스트")
    class DeleteReviewTest {

        @Test
        @DisplayName("리뷰 삭제 성공")
        void 리뷰삭제_성공() {
            logger.info("===== 리뷰삭제_성공 테스트 시작 =====");

            // given
            when(reviewRepository.findByReviewIdAndStatus(1000L, ReviewStatus.ACTIVE))
                .thenReturn(Optional.of(mockReview));

            // when
            reviewService.deleteReview(1000L);

            // then
            verify(reviewVisibilityRequestsRepository).updateVisibilityRequestStatusByReviewId(1000L, ReviewerResponse.AGREED);
            verify(reviewRepository).save(any(Review.class));
            assertEquals(ReviewStatus.DELETED, mockReview.getStatus());
        }

        @Test
        @DisplayName("리뷰 삭제 실패: 리뷰 없음")
        void 리뷰삭제_실패_리뷰없음() {
            logger.warn("===== 리뷰삭제_실패_리뷰없음 테스트 시작 =====");

            // given
            when(reviewRepository.findByReviewIdAndStatus(9999L, ReviewStatus.ACTIVE))
                .thenReturn(Optional.empty());

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                reviewService.deleteReview(9999L);
            });
            assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
            verify(reviewVisibilityRequestsRepository, never()).updateVisibilityRequestStatusByReviewId(anyLong(), any());
            verify(reviewRepository, never()).save(any(Review.class));
        }
    }

    @Nested
    @DisplayName("리뷰 블라인드 처리 테스트")
    class BlindReviewTest {

        @Test
        @DisplayName("리뷰 블라인드 성공")
        void 리뷰블라인드_성공() {
            logger.info("===== 리뷰블라인드_성공 테스트 시작 =====");

            // given
            when(reviewRepository.findByReviewIdAndStatus(1000L, ReviewStatus.ACTIVE))
                .thenReturn(Optional.of(mockReview));

            // when
            reviewService.blindReview(1000L, 7);

            // then
            verify(reviewRepository).save(any(Review.class));
            assertNotNull(mockReview.getBlindUntil());
        }

        @Test
        @DisplayName("리뷰 블라인드 실패: 리뷰 없음")
        void 리뷰블라인드_실패_리뷰없음() {
            logger.warn("===== 리뷰블라인드_실패_리뷰없음 테스트 시작 =====");

            // given
            when(reviewRepository.findByReviewIdAndStatus(9999L, ReviewStatus.ACTIVE))
                .thenReturn(Optional.empty());

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                reviewService.blindReview(9999L, 3);
            });
            assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
            verify(reviewRepository, never()).save(any(Review.class));
        }
    }
}