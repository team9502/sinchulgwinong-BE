package team9502.sinchulgwinong.domain.companyUser.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.companyUser.dto.response.CpUserProfileResponseDTO;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class CpUserService {

    private final CompanyUserRepository companyUserRepository;

    @Transactional(readOnly = true)
    public CpUserProfileResponseDTO getCpUserProfile(Long cpUserId) {

        CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        return new CpUserProfileResponseDTO(
                companyUser.getCpUserId(),
                companyUser.getCpName(),
                companyUser.getCpEmail(),
                companyUser.getCpPhoneNumber(),
                companyUser.getCpUsername(),
                companyUser.getHiringStatus(),
                companyUser.getEmployeeCount(),
                companyUser.getFoundationDate(),
                companyUser.getDescription());
    }
}
