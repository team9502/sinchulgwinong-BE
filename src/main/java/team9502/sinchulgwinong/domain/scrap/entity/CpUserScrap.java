package team9502.sinchulgwinong.domain.scrap.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.user.entity.User;

@Getter
@Entity
public class CpUserScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cpUserScrapId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "cp_user_id")
    private CompanyUser companyUser;

    @Setter
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;
}
