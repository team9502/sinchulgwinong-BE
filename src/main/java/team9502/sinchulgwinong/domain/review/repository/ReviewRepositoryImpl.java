package team9502.sinchulgwinong.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import team9502.sinchulgwinong.domain.review.entity.QReview;
import team9502.sinchulgwinong.domain.review.entity.Review;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> findAllReviewsByCompanyUserId(Long cpUserId, Pageable pageable) {

        QReview review = QReview.review;

        // 결과 목록 조회 쿼리
        List<Review> reviews = queryFactory
                .selectFrom(review)
                .where(review.cpUser.cpUserId.eq(cpUserId))
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 결과 수 계산 쿼리
        Long total = Optional.ofNullable(queryFactory
                .select(review.count())
                .from(review)
                .where(review.cpUser.cpUserId.eq(cpUserId))
                .fetchOne()).orElse(0L); // fetchOne() 결과가 null일 경우 기본값으로 0을 사용

        return new PageImpl<>(reviews, pageable, total);
    }
}