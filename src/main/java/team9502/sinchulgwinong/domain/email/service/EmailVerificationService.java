package team9502.sinchulgwinong.domain.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.email.entity.EmailVerifications;
import team9502.sinchulgwinong.domain.email.enums.UserType;
import team9502.sinchulgwinong.domain.email.enums.VerificationStatus;
import team9502.sinchulgwinong.domain.email.repository.EmailVerificationRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;

    @Transactional
    public void createVerification(String email, UserType userType) {

        validateEmail(email);

        try {
            String code = generateVerificationCode();
            EmailVerifications verification = buildVerification(email, userType, code);
            emailVerificationRepository.save(verification);

            emailService.sendSimpleMessage(email, "[신출귀농] 이메일 인증 코드", "인증 코드: " + code);
        } catch (MailAuthenticationException exception) {
            throw new ApiException(ErrorCode.EMAIL_AUTH_FAILED);
        } catch (MailException exception) {
            throw new ApiException(ErrorCode.EMAIL_SEND_FAILED);
        } catch (Exception exception) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void verifyCode(String email, String code) {

        validateEmailAndCode(email, code);

        try {
            EmailVerifications verification = findPendingVerification(email, code);
            if (verification.getExpiresAt().before(new Date())) {
                throw new ApiException(ErrorCode.EXPIRED_VERIFICATION_CODE);
            }
            verification.setStatus(VerificationStatus.VERIFIED);
            emailVerificationRepository.save(verification);
        } catch (ApiException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public boolean isEmailVerified(String email) {

        return emailVerificationRepository.existsByEmailAndStatus(email, VerificationStatus.VERIFIED);
    }

    @Scheduled(cron = "0 0 * * * *") // 1시간마다 실행
    @Transactional
    public void handleExpiredVerifications() {

        try {
            updateExpiredVerifications();
            deleteExpiredVerifications();
        } catch (Exception exception) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void updateExpiredVerifications() {

        Date now = new Date();
        try {
            List<EmailVerifications> expiredVerifications = emailVerificationRepository.findByExpiresAtBeforeAndStatus(now, VerificationStatus.PENDING);
            for (EmailVerifications verification : expiredVerifications) {
                verification.setStatus(VerificationStatus.EXPIRED);
            }
            emailVerificationRepository.saveAll(expiredVerifications);
        } catch (Exception exception) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void deleteExpiredVerifications() {

        Date now = new Date();
        try {
            emailVerificationRepository.deleteByExpiresAtBeforeAndStatus(now, VerificationStatus.EXPIRED);
        } catch (Exception exception) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String generateVerificationCode() {

        return UUID.randomUUID().toString().substring(0, 6);
    }

    private void validateEmail(String email) {

        if (email == null || email.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
    }

    private void validateEmailAndCode(String email, String code) {

        if (email == null || email.isEmpty() || code == null || code.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
    }

    private EmailVerifications findPendingVerification(String email, String code) {

        return emailVerificationRepository.findByEmailAndVerificationCodeAndExpiresAtAfterAndStatus(
                        email, code, new Date(), VerificationStatus.PENDING)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_VERIFICATION_CODE));
    }

    private EmailVerifications buildVerification(String email, UserType userType, String code) {

        return EmailVerifications.builder()
                .email(email)
                .userType(userType)
                .verificationCode(code)
                .createdAt(new Date())
                .expiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5분
                .status(VerificationStatus.PENDING)
                .build();
    }
}
