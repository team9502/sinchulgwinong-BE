package team9502.sinchulgwinong.domain.jobBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.jobBoard.entity.AdJobBoard;

public interface AdJobBoardRepository extends JpaRepository<AdJobBoard, Long> {

    AdJobBoard findByJobBoard_JobBoardId(Long jobBoardId);
}
