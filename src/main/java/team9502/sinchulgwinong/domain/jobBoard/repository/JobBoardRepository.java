package team9502.sinchulgwinong.domain.jobBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;

import java.time.LocalDate;
import java.util.List;

public interface JobBoardRepository extends JpaRepository<JobBoard, Long> {

    List<JobBoard> findByCompanyUser_CpUserId(Long cpUserId);

    List<JobBoard> findByJobEndDateBefore(LocalDate date);
}
