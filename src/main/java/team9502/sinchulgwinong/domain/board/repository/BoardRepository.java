package team9502.sinchulgwinong.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team9502.sinchulgwinong.domain.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
