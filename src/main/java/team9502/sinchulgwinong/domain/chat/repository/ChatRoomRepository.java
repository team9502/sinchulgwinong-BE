package team9502.sinchulgwinong.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.chat.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByUser_UserId(Long userId);

    List<ChatRoom> findByCompanyUser_CpUserId(Long cpUserId);

    boolean existsByUser_UserIdAndCompanyUser_CpUserId(Long userId, Long cpUserId);
}
