package team9502.sinchulgwinong.domain.category.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Locality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long localityId;

    @Column(nullable = false)
    private String regionName;

    @Column(nullable = false)
    private String subRegionName;

    @Column(nullable = false)
    private String localityName;
}
