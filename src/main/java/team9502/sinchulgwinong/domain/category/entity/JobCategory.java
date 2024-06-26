package team9502.sinchulgwinong.domain.category.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class JobCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobCategoryId;

    @Column(nullable = false)
    private String majorCategoryName;

    @Column(nullable = false)
    private String minorCategoryName;
}
