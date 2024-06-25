package team9502.sinchulgwinong.domain.jobBoard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team9502.sinchulgwinong.domain.category.entity.Locality;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class JobBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobBoardId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "cpUserId")
    private CompanyUser companyUser;

    @Setter
    @OneToOne
    @JoinColumn(name = "localityId")
    private Locality locality;

    @Setter
    @OneToMany(mappedBy = "jobBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> boardImage = new ArrayList<>();

    @Setter
    @Column(nullable = false)
    private String cpName;

    @Setter
    @Column(length = 100, nullable = false)
    private String jobTitle;

    @Setter
    @Column(length = 1000)
    private String jobContent;

    @Setter
    @Column(nullable = false)
    private LocalDate jobStartDate;

    @Setter
    @Column(nullable = false)
    private LocalDate jobEndDate;

    @Setter
    @Column(nullable = false)
    private Integer salaryAmount;

    @Setter
    @Column(nullable = false)
    private String sex;

    @Setter
    @Column(nullable = false)
    private String address;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus jobStatus;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalaryType salaryType;

}
