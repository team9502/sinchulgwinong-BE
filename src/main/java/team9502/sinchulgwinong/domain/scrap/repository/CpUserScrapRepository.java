package team9502.sinchulgwinong.domain.scrap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.scrap.entity.CpUserScrap;

public interface CpUserScrapRepository extends JpaRepository<CpUserScrap, Long> {

    boolean existsByCompanyUser_CpUserIdAndUser_UserId(Long cpUserId, Long userId);

    void deleteByUser_UserIdAndCompanyUser_CpUserId(Long userId, Long cpUserId);

    Page<CpUserScrap> findByUser_UserId(Long userId, Pageable pageable);

    void deleteByUser_UserId(Long userId);
}
