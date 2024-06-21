package team9502.sinchulgwinong.domain.scrap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.scrap.entity.JobScrap;

public interface JobScrapRepository extends JpaRepository<JobScrap, Long> {

    boolean existsByJobBoard_JobBoardIdAndUser_UserId(Long jobBoardId, Long userId);

    JobScrap findByJobBoard_JobBoardIdAndUser_UserId(Long jobBoardId, Long userId);

    Page<JobScrap> findByUser_UserId(Long userId, Pageable pageable);
}
