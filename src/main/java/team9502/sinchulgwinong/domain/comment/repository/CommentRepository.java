package team9502.sinchulgwinong.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team9502.sinchulgwinong.domain.comment.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBoard_BoardId(Long boardId);

    List<Comment> findByUser_UserId(Long userId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.board.boardId = :boardId")
    Long countCommentsByBoardId(@Param("boardId") Long boardId);
}
