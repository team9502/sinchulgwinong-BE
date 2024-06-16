package team9502.sinchulgwinong.domain.email.service;

import lombok.RequiredArgsConstructor;
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

        String code = generateVerificationCode();
        EmailVerifications verification = EmailVerifications.builder()
                .email(email)
                .userType(userType)
                .verificationCode(code)
                .createdAt(new Date())
                .expiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5분
                .status(VerificationStatus.PENDING)
                .build();
        emailVerificationRepository.save(verification);

        emailService.sendSimpleMessage(email, "[신출귀농] 이메일 인증 코드", "인증 코드: " + code);
    }

    @Transactional
    public void verifyCode(String email, String code) {

        EmailVerifications verification = emailVerificationRepository.findByEmailAndVerificationCodeAndExpiresAtAfterAndStatus(
                        email, code, new Date(), VerificationStatus.PENDING)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_OR_EXPIRED_VERIFICATION_CODE));

        verification.setStatus(VerificationStatus.VERIFIED);
        emailVerificationRepository.save(verification);
    }

    @Scheduled(cron = "0 0 * * * *") // 1시간마다 실행
    @Transactional
    public void handleExpiredVerifications() {

        updateExpiredVerifications(); // 만료된 인증 코드를 EXPIRED 상태로 업데이트
        deleteExpiredVerifications(); // EXPIRED 상태의 인증 코드를 삭제
    }

    @Transactional
    public void updateExpiredVerifications() {

        Date now = new Date();
        List<EmailVerifications> expiredVerifications = emailVerificationRepository.findByExpiresAtBeforeAndStatus(now, VerificationStatus.PENDING);
        for (EmailVerifications verification : expiredVerifications) {
            verification.setStatus(VerificationStatus.EXPIRED);
        }
        emailVerificationRepository.saveAll(expiredVerifications);
    }

    @Transactional
    public void deleteExpiredVerifications() {

        Date now = new Date();
        emailVerificationRepository.deleteByExpiresAtBeforeAndStatus(now, VerificationStatus.EXPIRED);
    }

    private String generateVerificationCode() {

        return UUID.randomUUID().toString().substring(0, 6);
    }
}
