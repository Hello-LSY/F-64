package F64.Board.Comment;

import F64.Board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBoardOrderByCreatedDateDesc(Board board);

    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.board.id = :boardId")
    void deleteByBoardId(Long boardId);
}
