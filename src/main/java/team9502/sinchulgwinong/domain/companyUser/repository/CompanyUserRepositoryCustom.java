package team9502.sinchulgwinong.domain.companyUser.repository;

import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;

import java.util.List;

public interface CompanyUserRepositoryCustom {

    List<CompanyUser> findAllWithFilters(String sort, Float minRating, Float maxRating);

    long countScrapsByCompanyUserId(Long companyId);
}
