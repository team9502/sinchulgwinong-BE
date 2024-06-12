package team9502.sinchulgwinong.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import team9502.sinchulgwinong.domain.review.entity.QReview;
import team9502.sinchulgwinong.domain.review.entity.Review;

import java.util.List;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> findAllReviewsByCompanyUserId(Long cpUserId, Pageable pageable) {
        QReview review = QReview.review;

        List<Review> reviews = queryFactory.selectFrom(review)
                .where(review.cpUser.cpUserId.eq(cpUserId))
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(review)
                .where(review.cpUser.cpUserId.eq(cpUserId))
                .fetchCount();

        return new PageImpl<>(reviews, pageable, total);
    }
}