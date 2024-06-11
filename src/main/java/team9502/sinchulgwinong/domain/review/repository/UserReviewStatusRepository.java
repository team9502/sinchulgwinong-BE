package team9502.sinchulgwinong.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.review.entity.Review;
import team9502.sinchulgwinong.domain.review.entity.UserReviewStatus;
import team9502.sinchulgwinong.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserReviewStatusRepository extends JpaRepository<UserReviewStatus, Long> {

    Optional<UserReviewStatus> findByUserAndReview(User user, Review review);

    List<UserReviewStatus> findByUserAndReviewIn(User user, List<Review> reviews);
}
