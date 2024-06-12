package team9502.sinchulgwinong.domain.jobBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;

public interface JobBoardRepository extends JpaRepository<JobBoard, Long> {
}
