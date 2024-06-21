package team9502.sinchulgwinong.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import team9502.sinchulgwinong.domain.review.enums.ReviewerResponse;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ReviewVisibilityRequests")
public class ReviewVisibilityRequests extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId", nullable = false)
    private Review review;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReviewerResponse reviewerResponse = ReviewerResponse.UNANSWERED;
}
