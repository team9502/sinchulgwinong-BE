package team9502.sinchulgwinong.domain.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.email.entity.EmailVerifications;
import team9502.sinchulgwinong.domain.email.enums.VerificationStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerifications, Long> {

    Optional<EmailVerifications> findByEmailAndVerificationCodeAndExpiresAtAfterAndStatus(String email, String verificationCode, Date now, VerificationStatus status);

    List<EmailVerifications> findByExpiresAtBeforeAndStatus(Date expiresAt, VerificationStatus status);

    void deleteByExpiresAtBeforeAndStatus(Date expiresAt, VerificationStatus status);

    boolean existsByEmailAndStatus(String email, VerificationStatus status);
}
