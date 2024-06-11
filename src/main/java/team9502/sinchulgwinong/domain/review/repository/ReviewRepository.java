package team9502.sinchulgwinong.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.review.entity.Review;
import team9502.sinchulgwinong.domain.user.entity.User;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    boolean existsByUserAndCpUser(User user, CompanyUser cpUser);

    List<Review> findByCpUser_CpUserId(Long cpUserId);

    Page<Review> findReviewsByCpUser_CpUserId(Long cpUserId, Pageable pageable);

}
