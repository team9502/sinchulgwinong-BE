package team9502.sinchulgwinong.domain.faq.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team9502.sinchulgwinong.domain.faq.entity.Faq;

public interface FaqRepositoryCustom {

    Page<Faq> findAllFaqs(Pageable pageable);
}
