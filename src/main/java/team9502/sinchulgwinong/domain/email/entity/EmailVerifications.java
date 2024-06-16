package team9502.sinchulgwinong.domain.email.entity;

import jakarta.persistence.*;
import lombok.*;
import team9502.sinchulgwinong.domain.email.enums.UserType;
import team9502.sinchulgwinong.domain.email.enums.VerificationStatus;

import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EmailVerifications", indexes = {
        @Index(name = "idx_expires_at_status", columnList = "expiresAt, status")
})
public class EmailVerifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    @Column(nullable = false, length = 250)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column
    private Long userReferenceId;

    @Column(nullable = false, length = 6)
    private String verificationCode;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VerificationStatus status;
}
