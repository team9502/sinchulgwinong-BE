package team9502.sinchulgwinong.domain.companyUser.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;

public interface CompanyUserRepositoryCustom {

    Page<CompanyUser> findAllWithFilters(String sort, Float minRating, Float maxRating, Pageable pageable);

    long countScrapsByCompanyUserId(Long cpUserId);
}
