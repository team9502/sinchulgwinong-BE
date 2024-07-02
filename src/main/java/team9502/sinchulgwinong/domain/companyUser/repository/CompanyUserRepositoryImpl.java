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
import team9502.sinchulgwinong.domain.jobBoard.entity.QJobBoard;
import team9502.sinchulgwinong.domain.scrap.entity.QCpUserScrap;

import java.util.List;

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
        QCpUserScrap cpUserScrap = QCpUserScrap.cpUserScrap;
        QJobBoard jobBoard = QJobBoard.jobBoard;

        // 기본 쿼리 구성
        JPAQuery<CompanyUser> query = queryFactory
                .selectFrom(companyUser)
                .leftJoin(cpUserScrap).on(cpUserScrap.companyUser.eq(companyUser))
                .leftJoin(jobBoard).on(jobBoard.companyUser.eq(companyUser));

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
                case "jobPostingsDesc":
                    query.orderBy(jobBoard.count().desc());
                    break;
                case "viewsDesc":
                    query.orderBy(companyUser.viewCount.desc());
                    break;
                case "scrapsDesc":
                    query.orderBy(cpUserScrap.count().desc());
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

        // 총 개수 계산을 위한 쿼리 생성 및 실행
        long total = query.fetchCount();

        // 쿼리 디버깅을 위한 로그
        logger.info("쿼리 실행 조건 - sort: {}, minRating: {}, maxRating: {}, pageable: {}, total: {}", sort, minRating, maxRating, pageable, total);

        // 페이지 데이터 조회
        List<CompanyUser> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

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
