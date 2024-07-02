package team9502.sinchulgwinong.domain.companyUser.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.entity.QCompanyUser;
import team9502.sinchulgwinong.domain.scrap.entity.QCpUserScrap;

import java.util.List;
import java.util.Optional;

@Repository
public class CompanyUserRepositoryImpl implements CompanyUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final Logger logger = LoggerFactory.getLogger(CompanyUserRepositoryImpl.class);

    public CompanyUserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<CompanyUser> findAllWithFilters(String sort, Float minRating, Float maxRating, Pageable pageable) {
        QCompanyUser companyUser = QCompanyUser.companyUser;

        // 기본 쿼리 구성
        JPAQuery<CompanyUser> query = queryFactory
                .selectFrom(companyUser);

        // 필터 조건 적용
        if (minRating != null && maxRating != null) {
            query.where(companyUser.averageRating.between(minRating, maxRating));
        }

        // 정렬 조건 적용
        if (sort != null) {
            switch (sort) {
                case "reviewsDesc":
                    query.orderBy(companyUser.reviewCount.desc());
                    break;
                case "viewsDesc":
                    query.orderBy(companyUser.viewCount.desc());
                    break;
                case "createdAtDesc":
                    query.orderBy(companyUser.createdAt.desc());
                    break;
                default:
                    query.orderBy(companyUser.cpUserId.asc()); // 기본 정렬 조건
            }
        } else {
            query.orderBy(companyUser.cpUserId.asc()); // 기본 정렬 조건
        }

        // 페이지 데이터 조회
        List<CompanyUser> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 계산
        long total = Optional.ofNullable(queryFactory
                .select(companyUser.count())
                .from(companyUser)
                .fetchOne()).orElse(0L);

        // 쿼리 디버깅을 위한 로그
        logger.info("쿼리 실행 조건 - sort: {}, minRating: {}, maxRating: {}, pageable: {}, total: {}", sort, minRating, maxRating, pageable, total);

        // 결과 디버깅을 위한 로그
        logger.info("쿼리 결과 - results size: {}, pageable: {}", results.size(), pageable);

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public long countScrapsByCompanyUserId(Long cpUserId) {
        QCpUserScrap scrap = QCpUserScrap.cpUserScrap;
        Long count = queryFactory
                .select(scrap.count())
                .from(scrap)
                .where(scrap.companyUser.cpUserId.eq(cpUserId))
                .fetchOne();

        // fetchOne() 결과가 null일 경우 0으로 대체
        return count != null ? count : 0L;
    }
}
