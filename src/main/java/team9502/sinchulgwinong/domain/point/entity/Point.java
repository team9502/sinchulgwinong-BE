package team9502.sinchulgwinong.domain.point.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    @Setter
    @Column(nullable = false)
    private Integer point;

    public Point(Integer initialPoint) {
        this.point = initialPoint;
    }
}
