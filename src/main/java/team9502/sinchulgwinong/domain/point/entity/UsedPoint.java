package team9502.sinchulgwinong.domain.point.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.point.enums.UpType;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UsedPoints")
public class UsedPoint extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long upId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pointId", nullable = false)
    private Point point;

    @Column(nullable = false)
    private Integer upAmount;

    @Column(nullable = false)
    private Integer upBalance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UpType upType;
}
