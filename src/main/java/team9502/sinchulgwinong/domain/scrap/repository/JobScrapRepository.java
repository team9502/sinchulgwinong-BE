package team9502.sinchulgwinong.domain.scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.scrap.entity.JobScrap;

import java.util.List;

public interface JobScrapRepository extends JpaRepository<JobScrap, Long> {

    boolean existsByJobBoard_JobBoardIdAndUser_UserId(Long jobBoardId, Long userId);
    JobScrap findByJobBoard_JobBoardIdAndUser_UserId(Long jobBoardId, Long userId);
    List<JobScrap> findByUser_UserId(Long userId);
}
