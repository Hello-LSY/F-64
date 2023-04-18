package F64.Board;

import F64.Board.Comment.Comment;
import F64.Board.Comment.CommentRepository;
import F64.Board.Deleted.DeletedBoard;
import F64.Board.Deleted.DeletedBoardRepository;
import F64.Board.Like.BoardLike;
import F64.Board.Like.BoardLikeRepository;
import F64.User.Member;
import F64.User.UserRepository;
import F64.User.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import F64.User.CustomUser;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardLikeRepository boardLikeRepository;
    @Autowired
    private UserSecurityService userSecurityService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeletedBoardRepository deletedBoardRepository;
    @Autowired
    private CommentRepository commentRepository;

    //게시글 쓰기
    public void writeBoard(Board board) {
        board.setCreatedDate(LocalDate.now());

        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user.getNickname();
        board.setWriterNickname(nickname);
        board.setLikeCount(0);
        board.setViewCount(0);
        board.setWriterUsername(user.getUsername());

        boardRepository.save(board);
    }

    //게시글 보기
    public Board getBoardAndIncreaseViewCount(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        board.setViewCount(board.getViewCount()+1);
        return boardRepository.save(board);
    }

    public void deleteBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
        DeletedBoard deletedBoard = new DeletedBoard();
        deletedBoard.setBoard(board);
        deletedBoard.setContent(board.getContent());
        deletedBoard.setTitle(board.getTitle());
        deletedBoard.setWriterNickname(board.getWriterNickname());
        deletedBoard.setWriterUsername(board.getWriterUsername());
        deletedBoard.setCreatedDate(board.getCreatedDate());
        deletedBoard.setLikeCount(board.getLikeCount());
        deletedBoard.setViewCount(board.getViewCount());
        deletedBoardRepository.save(deletedBoard);

        //댓글도 삭제 추가
        commentRepository.deleteByBoardId(boardId);
        boardLikeRepository.deleteByBoardId(boardId);
        boardRepository.deleteById(boardId);
    }

    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

    public Board getBoardById(Long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. "));
    }

    public void updateBoard(Long id, Board updateBoard){
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
        board.setTitle(updateBoard.getTitle());
        board.setContent(updateBoard.getContent());

        boardRepository.save(board);
    }

    public boolean likeBoard(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board id: " + boardId));

        if (boardLikeRepository.findByBoardAndMember(board, member).isPresent()) {
            // 중복 추천일 경우 false 반환
            return false;
        }

        BoardLike boardLike = new BoardLike();
        boardLike.setBoard(board);
        boardLike.setMember(member);
        boardLikeRepository.save(boardLike);

        // 해당 게시글(Board)에 대한 추천 수 증가
        board.setLikeCount(board.getLikeCount() + 1);
        boardRepository.save(board);

        // 정상 처리일 경우 true 반환
        return true;
    }

    public void saveComment(Long memberId, Long boardId, String content){
        Member member = userRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("해당 맴버가 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setBoard(board);
        comment.setMember(member);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setWriterNickname(member.getNickname());
        comment.setWriterUsername(member.getUsername());

        commentRepository.save(comment);
    }

    public List<Comment> getCommentList(Long boardId) {
        //boardId로 찾은거 optionalBoard에 저장
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()) {
            //있으면 board에 get함
            Board board = optionalBoard.get();
            return commentRepository.findByBoardOrderByCreatedDateDesc(board);
        }
        return Collections.emptyList();
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));
    }

    public void updateComment(Long boardId, Long commentId)
    {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));
        commentRepository.deleteById(commentId);
    }
}