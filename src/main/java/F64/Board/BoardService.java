package F64.Board;

import F64.Board.Deleted.DeletedBoard;
import F64.Board.Deleted.DeletedBoardRepository;
import F64.Board.Like.BoardLike;
import F64.Board.Like.BoardLikeRepository;
import F64.User.Member;
import F64.User.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import F64.User.CustomUser;
import java.time.LocalDate;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardLikeRepository boardLikeRepository;
    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private DeletedBoardRepository deletedBoardRepository;


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

    public void deleteBoard(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
        DeletedBoard deletedBoard = new DeletedBoard();
        deletedBoard.setBoardId(board.getId());
        deletedBoard.setContent(board.getContent());
        deletedBoard.setTitle(board.getTitle());
        deletedBoard.setWriterNickname(board.getWriterNickname());
        deletedBoard.setWriterUsername(board.getWriterUsername());
        deletedBoard.setCreatedDate(board.getCreatedDate());
        deletedBoard.setLikeCount(board.getLikeCount());
        deletedBoard.setViewCount(board.getViewCount());
        deletedBoardRepository.save(deletedBoard);

        boardLikeRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
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



}