package team9502.sinchulgwinong.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.review.entity.Review;
import team9502.sinchulgwinong.domain.user.entity.User;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserAndCpUser(User user, CompanyUser cpUser);

    List<Review> findByCpUser_CpUserId(Long cpUserId);

}
