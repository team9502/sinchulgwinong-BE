package team9502.sinchulgwinong.domain.scrap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.scrap.entity.CpUserScrap;

public interface CpUserScrapRepository extends JpaRepository<CpUserScrap, Long> {

    boolean existsByCompanyUser_CpUserIdAndUser_UserId(Long cpUserId, Long userId);

    CpUserScrap findByCompanyUser_CpUserIdAndUser_UserId(Long cpUserId, Long userId);

    Page<CpUserScrap> findByUser_UserId(Long userId, Pageable pageable);
}
