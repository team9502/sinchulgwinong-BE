package team9502.sinchulgwinong.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.review.entity.ReviewVisibilityRequests;
import team9502.sinchulgwinong.domain.review.enums.ReviewerResponse;

public interface ReviewVisibilityRequestsRepository extends JpaRepository<ReviewVisibilityRequests, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE ReviewVisibilityRequests r SET r.reviewerResponse = ?2 WHERE r.review.reviewId = ?1")
    void updateVisibilityRequestStatusByReviewId(Long reviewId, ReviewerResponse status);
}