package team9502.sinchulgwinong.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.review.enums.ReviewStatus;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Reviews")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpUserId", nullable = false)
    private CompanyUser cpUser;

    @Setter
    @Column(nullable = false, length = 100)
    private String reviewTitle;

    @Setter
    @Column(nullable = false, length = 1000)
    private String reviewContent;

    @Setter
    @Column(nullable = false)
    private Integer rating;

    // 리뷰가 숨겨지는 기간 관리 필드
    @Setter
    private LocalDateTime blindUntil;

    @Setter
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'ACTIVE'")
    private ReviewStatus status = ReviewStatus.ACTIVE;

    @Version
    @Column(name = "version")
    private Long version;
}
