package team9502.sinchulgwinong.domain.companyUser.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.entity.QCompanyUser;
import team9502.sinchulgwinong.domain.scrap.entity.QCpUserScrap;

import java.util.List;

@Repository
public class CompanyUserRepositoryImpl implements CompanyUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CompanyUserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<CompanyUser> findAllWithFilters(String sort, Float minRating, Float maxRating, Pageable pageable) {
        QCompanyUser companyUser = QCompanyUser.companyUser;
        var query = queryFactory
                .selectFrom(companyUser)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if (minRating != null && maxRating != null) {
            query.where(companyUser.averageRating.between(minRating, maxRating));
        }

        if (sort != null) {
            switch (sort) {
                case "reviewsDesc":
                    query.orderBy(companyUser.reviewCount.desc());
                    break;
                case "createdAtDesc":
                default:
                    query.orderBy(companyUser.createdAt.desc());
                    break;
            }
        }

        List<CompanyUser> results = query.fetch();
        long total = queryFactory.selectFrom(companyUser).fetch().size();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public long countScrapsByCompanyUserId(Long cpUserId) {
        QCpUserScrap scrap = QCpUserScrap.cpUserScrap;
        return queryFactory
                .select(scrap.count())
                .from(scrap)
                .where(scrap.companyUser.cpUserId.eq(cpUserId))
                .fetchOne();
    }
}
