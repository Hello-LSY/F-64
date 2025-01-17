package F64.Board.Comment;

import F64.Board.Board;
import F64.Board.BoardRepository;
import F64.User.Member;
import F64.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveComment(Long memberId, Long boardId, String content) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .content(content)
                .board(board)
                .member(member)
                .createdDate(LocalDateTime.now())
                .writerNickname(member.getNickname())
                .writerUsername(member.getUsername())
                .build();

        commentRepository.save(comment);
        log.info("댓글 저장 완료: {}", comment.getId());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다.");
        }
        commentRepository.deleteById(commentId);
        log.info("댓글 삭제 완료: {}", commentId);
    }

    public List<Comment> getCommentList(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return commentRepository.findByBoardOrderByCreatedDateDesc(board);
    }
}
