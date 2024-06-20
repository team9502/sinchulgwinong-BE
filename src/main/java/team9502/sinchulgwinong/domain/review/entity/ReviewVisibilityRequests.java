package team9502.sinchulgwinong.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import team9502.sinchulgwinong.domain.review.enums.ReviewerResponse;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ReviewVisibilityRequests")
public class ReviewVisibilityRequests {

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
    private ReviewerResponse reviewerResponse = ReviewerResponse.UNANSWERED;
}
