package team9502.sinchulgwinong.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.domain.review.dto.request.ReviewCreationRequestDTO;
import team9502.sinchulgwinong.domain.review.dto.response.ReviewCreationResponseDTO;
import team9502.sinchulgwinong.domain.review.entity.Review;
import team9502.sinchulgwinong.domain.review.entity.UserReviewStatus;
import team9502.sinchulgwinong.domain.review.repository.ReviewRepository;
import team9502.sinchulgwinong.domain.review.repository.UserReviewStatusRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

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
}
