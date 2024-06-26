package team9502.sinchulgwinong.domain.jobBoard.dto.response;

import lombok.Getter;
import team9502.sinchulgwinong.domain.jobBoard.entity.BoardImage;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobStatus;
import team9502.sinchulgwinong.domain.jobBoard.entity.SalaryType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JobBoardResponseDTO {

    private Long jobBoardId;

    private Long cpUserId;

    private Long localityId;

    private Long jobCategoryId;

    private String cpName;

    private String jobTitle;

    private String jobContent;

    private LocalDate jobStartDate;

    private LocalDate jobEndDate;

    private Integer salaryAmount;

    private String sex;

    private String address;

    private JobStatus jobStatus;

    private SalaryType salaryType;

    private List<String> accessUrls;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public JobBoardResponseDTO(JobBoard jobBoard) {
        this.jobBoardId = jobBoard.getJobBoardId();
        this.cpUserId = jobBoard.getCompanyUser().getCpUserId();
        if (jobBoard.getLocality() != null) {
            this.localityId = jobBoard.getLocality().getLocalityId();
        } else {
            this.localityId = null;
        }
        if (jobBoard.getJobCategory() != null) {
            this.jobCategoryId = jobBoard.getJobCategory().getJobCategoryId();
        } else {
            this.jobCategoryId = null;
        }
        this.cpName = jobBoard.getCpName();
        this.jobTitle = jobBoard.getJobTitle();
        this.jobContent = jobBoard.getJobContent();
        this.salaryType = jobBoard.getSalaryType();
        this.jobStatus = jobBoard.getJobStatus();
        this.address = jobBoard.getAddress();
        this.sex = jobBoard.getSex();
        this.salaryAmount = jobBoard.getSalaryAmount();
        this.jobStartDate = jobBoard.getJobStartDate();
        this.jobEndDate = jobBoard.getJobEndDate();
        this.accessUrls = jobBoard.getBoardImage().stream()
                .map(BoardImage::getAccessUrl)
                .collect(Collectors.toList());
        this.createdAt = jobBoard.getCreatedAt();
        this.modifiedAt = jobBoard.getModifiedAt();
    }
}
