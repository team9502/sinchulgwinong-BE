package team9502.sinchulgwinong.domain.faq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.faq.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long> {
}
