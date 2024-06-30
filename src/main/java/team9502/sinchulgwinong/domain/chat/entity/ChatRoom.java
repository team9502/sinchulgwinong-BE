package team9502.sinchulgwinong.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

@Getter
@Entity
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Setter
    @ManyToOne
    @JoinColumn(name = "cp_user_id")
    private CompanyUser companyUser;

    @Setter
    @Column(nullable = false, length = 100)
    private String chatName;

    @Setter
    @Column(nullable = false)
    private boolean userRead; // 일반 사용자의 읽음 상태

    @Setter
    @Column(nullable = false)
    private boolean companyUserRead; // 회사 사용자의 읽음 상태
}
