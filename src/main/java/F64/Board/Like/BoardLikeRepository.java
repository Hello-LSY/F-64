package F64.Board.Like;

import F64.Board.Board;
import F64.Board.Like.BoardLike;
import F64.User.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Optional<BoardLike> findByBoardAndMember(Board board, Member member);
}
