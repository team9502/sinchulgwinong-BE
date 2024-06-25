package team9502.sinchulgwinong.domain.jobBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobOpenApiDetailResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobOpenApiResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobStatus;
import team9502.sinchulgwinong.domain.jobBoard.entity.SalaryType;
import team9502.sinchulgwinong.domain.jobBoard.repository.JobBoardRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class JobBoardOpenApiService {

    private final JobBoardRepository jobBoardRepository;
    private final CompanyUserRepository companyUserRepository;
    private final WebClient webClient;

    @Transactional
    public void JobOpenApiSave(Long cpUserId) {

        CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        getOpenApiData().subscribe(response -> {
            if (response.getJobOpenApiResponseVO() != null) {
                response.getJobOpenApiResponseVO().getJobOpenApiDetailResponseDTOS()
                        .forEach(detail -> {
                            if (!isDuplicate(detail)) {
                                JobBoard jobBoard = convertToJobBoard(detail, companyUser);
                                jobBoardRepository.save(jobBoard);
                            }
                        });
            }
        });
    }

    private boolean isDuplicate(JobOpenApiDetailResponseDTO detail) {
        return jobBoardRepository.existsByCpNameAndJobTitle(detail.getCompanyName(), detail.getJobTitle());
    }

    public Mono<JobOpenApiResponseDTO> getOpenApiData() {
        return webClient.get()
                .uri("/Grid_20161229000000000477_1/83498/84398")
                .header("Content-type", "application/json")
                .retrieve()
                .bodyToMono(JobOpenApiResponseDTO.class);
    }

    private JobBoard convertToJobBoard(JobOpenApiDetailResponseDTO detail, CompanyUser companyUser) {

        JobBoard jobBoard = new JobBoard();

        jobBoard.setCompanyUser(companyUser);
        jobBoard.setCpName(detail.getCompanyName());
        jobBoard.setJobTitle(detail.getJobTitle());
        jobBoard.setJobContent(" ");
        jobBoard.setJobStartDate(convertToLocalDate(detail.getCreateDate()));
        jobBoard.setJobEndDate(convertToLocalDate(detail.getCloseDate()));
        jobBoard.setSalaryAmount(convertToInteger(detail.getSalaryAmount()));
        jobBoard.setSex(" ");
        jobBoard.setAddress(detail.getAddress());
        jobBoard.setJobStatus(JobStatus.JOBCLOSED);
        jobBoard.setSalaryType(SalaryType.MONTHLY);

        return jobBoard;
    }

    private LocalDate convertToLocalDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            return LocalDate.parse(dateString, formatter);
        } catch (Exception e) {

            return null;
        }
    }

    private Integer convertToInteger(String salaryString) {
        try {
            String numericPart = salaryString.replaceAll("[^0-9]", "");

            return Integer.valueOf(numericPart);
        } catch (NumberFormatException e) {

            return null;
        }
    }
}
