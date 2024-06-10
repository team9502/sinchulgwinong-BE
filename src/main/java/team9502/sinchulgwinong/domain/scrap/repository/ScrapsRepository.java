package team9502.sinchulgwinong.domain.scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.scrap.entity.Scrap;

import java.util.List;

public interface ScrapsRepository extends JpaRepository<Scrap, Long> {

    List<Scrap> findByUser_UserId (Long userId);

    boolean existsByUser_UserIdAndBoard_BoardId(Long userId, Long boardId);

    Scrap findByUser_UserIdAndBoard_BoardId(Long userId, Long boardId);
}
