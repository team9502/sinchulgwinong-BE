package team9502.sinchulgwinong.domain.companyUser.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.entity.QCompanyUser;
import team9502.sinchulgwinong.domain.scrap.entity.QCpUserScrap;

import java.util.List;

public class CompanyUserRepositoryImpl implements CompanyUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CompanyUserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<CompanyUser> findAllWithFilters(String sort, Float minRating, Float maxRating) {
        QCompanyUser companyUser = QCompanyUser.companyUser;
        var query = queryFactory
                .selectFrom(companyUser);

        if (minRating != null && maxRating != null) {
            query.where(companyUser.averageRating.between(minRating, maxRating));
        }

        if (sort != null) {
            if (sort.equals("reviewsDesc")) {
                query.orderBy(companyUser.reviewCount.desc());
            } else {
                query.orderBy(companyUser.createdAt.desc());
            }
        } else {
            // 기본 정렬이 없는 경우, createdAt으로 내림차순 정렬
            query.orderBy(companyUser.createdAt.desc());
        }

        return query.fetch();
    }

    @Override
    public long countScrapsByCompanyUserId(Long companyId) {
        QCpUserScrap scrap = QCpUserScrap.cpUserScrap;
        return queryFactory
                .select(scrap.count())
                .from(scrap)
                .where(scrap.companyUser.cpUserId.eq(companyId))
                .fetchOne();  // fetchCount() 대신 fetchOne() 사용
    }
}
