package team9502.sinchulgwinong.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import team9502.sinchulgwinong.domain.review.repository.ReviewRepository;
import team9502.sinchulgwinong.domain.review.repository.UserReviewStatusRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CompanyUserRepository companyUserRepository;
    private final UserRepository userRepository;
    private final UserReviewStatusRepository userReviewStatusRepository;
    private final PointService pointService;

    @Transactional
    public ReviewCreationResponseDTO createReview(Long userId, ReviewCreationRequestDTO requestDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        CompanyUser companyUser = companyUserRepository.findById(requestDTO.getCpUserId())
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        if (reviewRepository.existsByUserAndCpUser(user, companyUser)) {
            throw new ApiException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        Review review = Review.builder()
                .user(user)
                .cpUser(companyUser)
                .reviewTitle(requestDTO.getReviewTitle())
                .reviewContent(requestDTO.getReviewContent())
                .rating(requestDTO.getRating())
                .build();
        review = reviewRepository.save(review);

        UserReviewStatus status = UserReviewStatus.builder()
                .user(user)
                .review(review)
                .isPrivate(true)
                .build();
        userReviewStatusRepository.save(status);

        pointService.earnPoints(user, SpType.REVIEW);

        return new ReviewCreationResponseDTO(
                review.getReviewId(),
                companyUser.getCpUserId(),
                review.getReviewTitle(),
                review.getReviewContent(),
                review.getRating()
        );
    }

    @Transactional(readOnly = true)
    public ReviewListResponseDTO findAllReviewsByCompanyUserId(Long cpUserId) {

        List<Review> reviews = reviewRepository.findByCpUser_CpUserId(cpUserId);

        return new ReviewListResponseDTO(reviews);
    }

    @Transactional(readOnly = true)
    public UserReviewListResponseDTO getReviewsWithVisibility(Long cpUserId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        List<Review> reviews = reviewRepository.findByCpUser_CpUserId(cpUserId);
        List<UserReviewStatus> statuses = userReviewStatusRepository.findByUserAndReviewIn(user, reviews);

        return new UserReviewListResponseDTO(reviews, statuses);
    }


    @Transactional
    public ReviewResponseDTO viewReview(Long reviewId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorCode.REVIEW_NOT_FOUND));

        UserReviewStatus status = userReviewStatusRepository.findByUserAndReview(user, review)
                .orElse(null);

        if (status == null) {
            // 최초 리뷰 확인 시 포인트 차감 및 상태 저장
            pointService.deductPoints(user, UpType.REVIEW);
            status = UserReviewStatus.builder()
                    .user(user)
                    .review(review)
                    .isPrivate(false)
                    .build();
            userReviewStatusRepository.save(status);
        } else if (!status.getIsPrivate()) {
            throw new ApiException(ErrorCode.REVIEW_ALREADY_PUBLIC);
        } else {
            // 리뷰가 비공개 상태인 경우 포인트 차감 후 공개 처리
            pointService.deductPoints(user, UpType.REVIEW);
            status.setIsPrivate(false);
            userReviewStatusRepository.save(status);
        }

        return new ReviewResponseDTO(review);
    }
}