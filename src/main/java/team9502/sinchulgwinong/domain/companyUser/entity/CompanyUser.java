package team9502.sinchulgwinong.domain.companyUser.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CompanyUsers")
public class CompanyUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cpUserId;

    @Column(nullable = false)
    private Boolean hiringStatus;

    @Column
    private Integer employeeCount;

    @Column
    private LocalDate foundationDate;

    @Column
    private String description;

    @Column(nullable = false)
    private String cpNum;

    @Column(nullable = false, length = 100)
    private String cpName;

    @Column(nullable = false, length = 20)
    private String cpUsername;

    @Column(nullable = false, length = 250)
    private String cpEmail;

    @Column(nullable = false, length = 11)
    private String cpPhoneNumber;

    @Column(nullable = false, length = 150)
    private String cpPassword;
}
