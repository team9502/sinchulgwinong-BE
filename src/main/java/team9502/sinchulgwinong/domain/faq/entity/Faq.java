package team9502.sinchulgwinong.domain.faq.entity;

import jakarta.persistence.*;
import lombok.*;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Faqs")
public class Faq extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faqId;

    @Column(nullable = false, length = 100)
    private String faqTitle;

    @Column(nullable = false, length = 1000)
    private String faqContent;

    @Setter
    @Column(nullable = false)
    private int viewCount;

    public void incrementViewCount() {
        this.viewCount += 1;
    }
}
