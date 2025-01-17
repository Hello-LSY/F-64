package F64.Board.Like;

import F64.Board.Board;
import F64.Board.BoardRepository;
import F64.User.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public boolean likeBoard(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (boardLikeRepository.existsByBoardAndMember(board, member)) {
            log.warn("중복 추천 방지 - 사용자: {}, 게시글: {}", member.getUsername(), boardId);
            return false;
        }

        BoardLike boardLike = BoardLike.builder()
                .board(board)
                .member(member)
                .build();

        boardLikeRepository.save(boardLike);
        board.setLikeCount(board.getLikeCount() + 1);
        boardRepository.save(board);

        log.info("게시글 추천 완료 - 사용자: {}, 게시글: {}", member.getUsername(), boardId);
        return true;
    }
}
