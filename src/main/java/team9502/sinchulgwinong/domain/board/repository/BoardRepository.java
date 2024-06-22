package team9502.sinchulgwinong.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByBoardTitleContaining(String boardTitle, Pageable pageable);

    Page<Board> findByUser_UserId(Long userId, Pageable pageable);
}
