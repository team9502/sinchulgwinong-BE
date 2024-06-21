package team9502.sinchulgwinong.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobStatus;
import team9502.sinchulgwinong.domain.jobBoard.repository.JobBoardRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobStatusScheduler {

    private final JobBoardRepository jobBoardRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void updateJobStatus() {
        LocalDate now = LocalDate.now();
        List<JobBoard> jobBoards = jobBoardRepository.findByJobEndDateBefore(now);

        for (JobBoard jobBoard : jobBoards) {
            jobBoard.setJobStatus(JobStatus.JOBCLOSED);
            jobBoardRepository.save(jobBoard);
        }
    }
}