package team9502.sinchulgwinong.domain.faq.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import team9502.sinchulgwinong.domain.faq.entity.Faq;
import team9502.sinchulgwinong.domain.faq.entity.QFaq;

import java.util.List;

@RequiredArgsConstructor
public class FaqRepositoryImpl implements FaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Faq> findAllFaqs(Pageable pageable) {

        QFaq faq = QFaq.faq;

        List<Faq> faqs = queryFactory
                .selectFrom(faq)
                .orderBy(faq.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(faq.count())
                .from(faq)
                .fetchOne();

        return new PageImpl<>(faqs, pageable, total);
    }
}
