package team9502.sinchulgwinong.domain.scrap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.scrap.entity.BoardScrap;

public interface BoardScrapsRepository extends JpaRepository<BoardScrap, Long> {

    Page<BoardScrap> findByUser_UserId(Long userId, Pageable pageable);

    boolean existsByUser_UserIdAndBoard_BoardId(Long userId, Long boardId);

    void deleteByUser_UserIdAndBoard_BoardId(Long userId, Long boardId);

    void deleteByUser_UserId(Long userId);
}
