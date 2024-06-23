package team9502.sinchulgwinong.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByBoard_BoardId(Long boardId, Pageable pageable);

    Page<Comment> findByUser_UserId(Long userId, Pageable pageable);

    int countByBoard_BoardId(Long boardId);
}
