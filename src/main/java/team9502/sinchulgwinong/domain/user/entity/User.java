package team9502.sinchulgwinong.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import team9502.sinchulgwinong.domain.point.CommonPoint;
import team9502.sinchulgwinong.domain.point.entity.Point;
import team9502.sinchulgwinong.domain.user.enums.LoginType;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
@DynamicUpdate
public class User extends BaseTimeEntity implements CommonPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pointId")
    private Point point;

    @Setter
    @Column(nullable = false, length = 20)
    private String username;

    @Setter
    @Column(nullable = false, length = 100)
    private String nickname;

    @Setter
    @Column(nullable = false, length = 150)
    private String password;

    @Setter
    @Column(nullable = false, length = 250)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    @Setter
    @Column(length = 11)
    private String phoneNumber;
}
