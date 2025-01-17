package F64.Board.Like;

import F64.Board.Board;
import F64.User.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    boolean existsByBoardAndMember(Board board, Member member);

    @Transactional
    @Modifying
    @Query("DELETE FROM BoardLike bl WHERE bl.board.id = :boardId")
    void deleteByBoardId(Long boardId);
}
