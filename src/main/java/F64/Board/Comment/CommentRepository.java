package F64.Board.Comment;

import F64.Board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardOrderByCreatedDateDesc(Board board);

    void deleteByBoardId(Long boardId);
}
