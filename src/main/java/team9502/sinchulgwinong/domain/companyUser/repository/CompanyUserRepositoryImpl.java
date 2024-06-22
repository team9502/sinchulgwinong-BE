package team9502.sinchulgwinong.domain.companyUser.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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

    public CompanyUserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<CompanyUser> findAllWithFilters(String sort, Float minRating, Float maxRating, Pageable pageable) {
        QCompanyUser companyUser = QCompanyUser.companyUser;
        QCpUserScrap cpUserScrap = QCpUserScrap.cpUserScrap;
        QJobBoard jobBoard = QJobBoard.jobBoard;

        var query = queryFactory
                .select(companyUser)
                .from(companyUser)
                .leftJoin(cpUserScrap).on(cpUserScrap.companyUser.eq(companyUser))
                .leftJoin(jobBoard).on(jobBoard.companyUser.eq(companyUser))
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
                case "jobPostingsDesc":
                    query.groupBy(companyUser)
                            .orderBy(jobBoard.count().desc());
                    break;
                case "viewsDesc":
                    // viewCount를 기준으로 정렬
                    query.orderBy(companyUser.viewCount.desc());
                    break;
                case "scrapsDesc":
                    // 스크랩 수를 기준으로 정렬
                    query.groupBy(companyUser)
                            .orderBy(cpUserScrap.count().desc());
                    break;
                case "createdAtDesc":
                default:
                    query.orderBy(companyUser.createdAt.desc());
                    break;
            }
        }

        List<CompanyUser> results = query.fetch();
        long total = query.fetchCount();

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
