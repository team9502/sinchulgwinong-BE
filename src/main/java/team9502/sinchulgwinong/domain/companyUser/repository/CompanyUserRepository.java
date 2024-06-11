package team9502.sinchulgwinong.domain.companyUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;

import java.util.Optional;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {

    Optional<CompanyUser> findByCpEmail(String cpEmail);
}
