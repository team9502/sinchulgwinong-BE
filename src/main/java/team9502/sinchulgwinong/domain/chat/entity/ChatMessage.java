package team9502.sinchulgwinong.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

@Getter
@Entity
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 일반 사용자

    @Setter
    @ManyToOne
    @JoinColumn(name = "cp_user_id")
    private CompanyUser companyUser;

    @Setter
    @Column(nullable = false, length = 400)
    private String content;

    @Setter
    private String sendUserType;
}