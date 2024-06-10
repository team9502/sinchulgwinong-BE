package team9502.sinchulgwinong.domain.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

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

    @Column(nullable = false)
    private Boolean isPrivate;
}
