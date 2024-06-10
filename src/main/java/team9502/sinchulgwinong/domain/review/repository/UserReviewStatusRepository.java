package team9502.sinchulgwinong.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.review.entity.UserReviewStatus;

public interface UserReviewStatusRepository extends JpaRepository<UserReviewStatus, Long> {
}
