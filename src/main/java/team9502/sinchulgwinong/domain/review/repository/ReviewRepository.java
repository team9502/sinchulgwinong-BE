package team9502.sinchulgwinong.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.review.entity.Review;
import team9502.sinchulgwinong.domain.review.enums.ReviewStatus;
import team9502.sinchulgwinong.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCpUser_CpUserIdAndStatus(Long cpUserId, ReviewStatus status);

    Page<Review> findReviewsByCpUser_CpUserIdAndStatus(Long cpUserId, ReviewStatus status, Pageable pageable);

    Optional<Review> findByReviewIdAndStatus(Long reviewId, ReviewStatus status);

    boolean existsByUserAndCpUser(User user, CompanyUser cpUser);
}
