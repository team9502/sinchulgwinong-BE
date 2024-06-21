package team9502.sinchulgwinong.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.review.enums.ReviewStatus;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
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

    @Column(nullable = false, length = 100)
    private String reviewTitle;

    @Column(nullable = false, length = 1000)
    private String reviewContent;

    @Column(nullable = false)
    private Integer rating;

    // 리뷰가 숨겨지는 기간 관리 필드
    @Setter
    private LocalDateTime blindUntil;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status = ReviewStatus.ACTIVE;
}
