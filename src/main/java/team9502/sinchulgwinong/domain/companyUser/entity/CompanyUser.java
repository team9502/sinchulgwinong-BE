package team9502.sinchulgwinong.domain.companyUser.entity;

import jakarta.persistence.*;
import lombok.*;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.point.CommonPoint;
import team9502.sinchulgwinong.domain.point.entity.Point;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CompanyUsers")
public class CompanyUser extends BaseTimeEntity implements CommonPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cpUserId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pointId")
    private Point point;

    @Setter
    @Column(nullable = false)
    private Boolean hiringStatus;

    @Setter
    @Column
    private Integer employeeCount;

    @Column
    private LocalDate foundationDate;

    @Setter
    @Column
    private String description;

    @Column(nullable = false)
    private String cpNum;

    @Column(nullable = false, length = 100)
    private String cpName;

    @Column(nullable = false, length = 20)
    private String cpUsername;

    @Setter
    @Column(nullable = false, length = 250)
    private String cpEmail;

    @Setter
    @Column(nullable = false, length = 11)
    private String cpPhoneNumber;

    @Setter
    @Column(nullable = false, length = 150)
    private String cpPassword;

    @Setter
    @Column
    @Builder.Default
    private Float averageRating = 0.0f;

    @Setter
    @Builder.Default
    private Integer reviewCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @OneToMany(mappedBy = "companyUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobBoard> jobBoards;

    public void incrementViewCount() {
        this.viewCount++;
    }
}
