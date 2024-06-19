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
import team9502.sinchulgwinong.domain.email.service.EmailVerificationService;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class CpUserService {

    private final CompanyUserRepository companyUserRepository;
    private final EncryptionService encryptionService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

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
                encryptionService.decryptCpNum(companyUser.getCpNum()),
                companyUser.getAverageRating(),
                companyUser.getReviewCount()
        );
    }

    @Transactional
    public CpUserProfileResponseDTO updateCpUserProfile(Long cpUserId, CpUserProfileUpdateRequestDTO requestDTO) {

        if (requestDTO == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }

        CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        if (requestDTO.getEmployeeCount() != null) {
            companyUser.setEmployeeCount(requestDTO.getEmployeeCount());
        }
        if (requestDTO.getHiringStatus() != null) {
            companyUser.setHiringStatus(requestDTO.getHiringStatus());
        }
        if (requestDTO.getDescription() != null) {
            companyUser.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getCpEmail() != null) {
            if (!emailVerificationService.isEmailVerified(requestDTO.getCpEmail())) {
                throw new ApiException(ErrorCode.EMAIL_NOT_VERIFIED);
            }
            companyUser.setCpEmail(requestDTO.getCpEmail());
        }
        if (requestDTO.getCpPhoneNumber() != null) {
            companyUser.setCpPhoneNumber(requestDTO.getCpPhoneNumber());
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
                encryptionService.decryptCpNum(companyUser.getCpNum()),
                companyUser.getAverageRating(),
                companyUser.getReviewCount()
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
