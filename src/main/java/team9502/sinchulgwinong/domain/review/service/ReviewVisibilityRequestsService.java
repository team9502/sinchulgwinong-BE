package team9502.sinchulgwinong.domain.review.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.email.service.EmailReceiverService;
import team9502.sinchulgwinong.domain.email.service.EmailService;
import team9502.sinchulgwinong.domain.review.dto.request.ReviewVisibilityRequestDTO;
import team9502.sinchulgwinong.domain.review.dto.response.ReviewVisibilityResponseDTO;
import team9502.sinchulgwinong.domain.review.entity.Review;
import team9502.sinchulgwinong.domain.review.entity.ReviewVisibilityRequests;
import team9502.sinchulgwinong.domain.review.entity.UserReviewStatus;
import team9502.sinchulgwinong.domain.review.enums.ReviewerResponse;
import team9502.sinchulgwinong.domain.review.repository.ReviewRepository;
import team9502.sinchulgwinong.domain.review.repository.ReviewVisibilityRequestsRepository;
import team9502.sinchulgwinong.domain.review.repository.UserReviewStatusRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ReviewVisibilityRequestsService {

    private final ReviewVisibilityRequestsRepository reviewVisibilityRequestsRepository;
    private final UserReviewStatusRepository userReviewStatusRepository;
    private final ReviewRepository reviewRepository;
    private final EmailService emailService;
    private final EmailReceiverService emailReceiverService;

    @Transactional
    public ReviewVisibilityResponseDTO createVisibilityRequest(ReviewVisibilityRequestDTO requestDTO, Long cpUserId) {
        Review review = reviewRepository.findById(requestDTO.getReviewId())
                .orElseThrow(() -> new ApiException(ErrorCode.REVIEW_NOT_FOUND));

        UserReviewStatus status = userReviewStatusRepository.findByUserAndReview(review.getUser(), review)
                .orElse(new UserReviewStatus());

        status.setReview(review);
        status.setUser(review.getUser());

        status.setIsPrivate(true);
        userReviewStatusRepository.save(status);

        ReviewVisibilityRequests visibilityRequest = ReviewVisibilityRequests.builder()
                .review(review)
                .reviewerResponse(ReviewerResponse.UNANSWERED)
                .build();
        reviewVisibilityRequestsRepository.save(visibilityRequest);

        sendEmailToReviewer(review, requestDTO);

        return new ReviewVisibilityResponseDTO(
                visibilityRequest.getRequestId(),
                cpUserId,
                requestDTO.getCpEmail(),
                LocalDateTime.now()
        );
    }

    private void sendEmailToReviewer(Review review, ReviewVisibilityRequestDTO requestDTO) {
        String reviewDate = review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        String tempActionDate = LocalDateTime.now().plusDays(30).format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        String emailContent = String.format("""
                        <p>회원님의 신출귀농 서비스 이용과 관련하여 회원님께서 작성한 리뷰에 대해 게시 중단 요청이 접수되어 아래와 같이 안내 드립니다.</p>
                        <p>1. 게시 중단 요청 대상 리뷰 : %s 작성 리뷰<br>
                        2. 기업명 : %s<br>
                        3. 게시 중단 요청 사유 : %s<br>
                        4. 임시 조치 일자 : %s<br>
                        5. 조치 내용 : 해당 게시물 임시 조치</p>
                        <p>임시 조치는 ‘정보통신망 이용촉진 및 정보보호 등에 관한 법률 제 44조의2 (정보의 삭제요청 등)’에 의거하여 리뷰에 대한 접근을 임시적으로 차단하는 것을 말합니다.</p>
                        <p>아래 내용은 해당 조치와 관련된 신출귀농 서비스 이용 정책입니다.</p>
                        <h3>리뷰 게시 중단 요청 정책</h3>
                        <ul>
                            <li>기업 회원은 소속된 기업의 부적절한 리뷰에 한해 게시 중단을 요청할 수 있습니다.</li>
                            <li>기업 회원은 리뷰 게시 중단 요청 시 합당한 리뷰 게시 중단 요청 사유서를 작성해야 합니다.
                                <ul>
                                    <li>리뷰 게시 중단 요청 사유가 근거 없는 비방, 인신 공격성 등 공익을 위한 리뷰가 아닌 경우 신출귀농 서비스 관리자의 판단 하에 해당 리뷰를 즉시 삭제합니다.</li>
                                    <li>위와 같은 리뷰 게시 중단 요청 사유가 아닐 경우, 신출귀농 서비스 관리자는 해당 리뷰를 임시적으로 차단하고, 해당 리뷰 작성자에게 메일로 관련 내용을 발송해야 합니다.
                                        <ul>
                                            <li>해당 리뷰 작성자가 리뷰 삭제에 동의할 경우, 리뷰를 즉시 삭제합니다.</li>
                                            <li>해당 리뷰 작성자가 리뷰 삭제에 비동의할 경우, 리뷰를 30일 간 임시 조치한 이후 재게시합니다.</li>
                                        </ul>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                        <p>리뷰 게시 중단 요청에 대한 동의 여부를 작성해 답신해주시면 신속히 확인 후 처리해드리도록 하겠습니다. <strong>명확한 입장을 표명하지 않을 경우</strong>, 리뷰 삭제에 비동의하는 것으로 간주해 <strong>리뷰를 30일 간 임시 조치한 이후 재게시</strong>합니다.</p>
                        <p>이 메일에 답장을 보내실 때 아래 리뷰 ID를 참조해 주세요. 리뷰 ID: %d</p>
                        <p>감사합니다.</p>
                        """,
                reviewDate, review.getCpUser().getCpName(), requestDTO.getReason(), tempActionDate, review.getReviewId());

        emailService.sendHtmlMessage(
                review.getUser().getEmail(),
                "[신출귀농] 리뷰 게시 중단 안내 메일",
                emailContent
        );
    }

    @Scheduled(fixedRate = 60000)
    public void checkVisibilityRequests() throws MessagingException {
        emailReceiverService.checkEmailForReviewResponses();
    }
}
