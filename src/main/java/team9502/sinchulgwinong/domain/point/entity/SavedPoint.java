package team9502.sinchulgwinong.domain.point.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SavedPoints")
public class SavedPoint extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pointId", nullable = false)
    private Point point;

    @Column(nullable = false)
    private Integer spAmount;

    @Column(nullable = false)
    private Integer spBalance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpType spType;
}
