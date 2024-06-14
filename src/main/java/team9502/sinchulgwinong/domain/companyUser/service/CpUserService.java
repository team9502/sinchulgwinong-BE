package team9502.sinchulgwinong.domain.companyUser.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.companyUser.dto.request.CpUserPasswordUpdateRequestDTO;
import team9502.sinchulgwinong.domain.companyUser.dto.request.CpUserProfileUpdateRequestDTO;
import team9502.sinchulgwinong.domain.companyUser.dto.response.CpUserProfileResponseDTO;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class CpUserService {

    private final CompanyUserRepository companyUserRepository;
    private final EncryptionService encryptionService;
    private final PasswordEncoder passwordEncoder;

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
                companyUser.getDescription(),
                encryptionService.decryptCpNum(companyUser.getCpNum())
        );
    }

    @Transactional
    public CpUserProfileResponseDTO updateCpUserProfile(Long cpUserId, CpUserProfileUpdateRequestDTO updateRequestDTO) {

        CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        if (updateRequestDTO.getEmployeeCount() != null) {
            companyUser.setEmployeeCount(updateRequestDTO.getEmployeeCount());
        }
        if (updateRequestDTO.getHiringStatus() != null) {
            companyUser.setHiringStatus(updateRequestDTO.getHiringStatus());
        }
        if (updateRequestDTO.getDescription() != null) {
            companyUser.setDescription(updateRequestDTO.getDescription());
        }
        if (updateRequestDTO.getCpEmail() != null) {
            companyUser.setCpEmail(updateRequestDTO.getCpEmail());
        }
        if (updateRequestDTO.getCpPhoneNumber() != null) {
            companyUser.setCpPhoneNumber(updateRequestDTO.getCpPhoneNumber());
        }

        return new CpUserProfileResponseDTO(
                companyUser.getCpUserId(),
                companyUser.getCpName(),
                companyUser.getCpEmail(),
                companyUser.getCpPhoneNumber(),
                companyUser.getCpUsername(),
                companyUser.getHiringStatus(),
                companyUser.getEmployeeCount(),
                companyUser.getFoundationDate(),
                companyUser.getDescription(),
                encryptionService.decryptCpNum(companyUser.getCpNum())
        );
    }

    @Transactional
    public void updateCpUserPassword(Long cpUserId, CpUserPasswordUpdateRequestDTO requestDTO) {

        CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDTO.getCurrentPassword(), companyUser.getCpPassword())) {
            throw new ApiException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (!requestDTO.getNewPassword().equals(requestDTO.getNewPasswordConfirm())) {
            throw new ApiException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }

        companyUser.setCpPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        companyUserRepository.save(companyUser);
    }
}
