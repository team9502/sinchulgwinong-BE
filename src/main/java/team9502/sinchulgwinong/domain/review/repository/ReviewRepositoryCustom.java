package team9502.sinchulgwinong.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team9502.sinchulgwinong.domain.review.entity.Review;

public interface ReviewRepositoryCustom {

    Page<Review> findAllReviewsByCompanyUserId(Long cpUserId, Pageable pageable);
}
