package F64.Board;

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


    //게시글 쓰기
    public void write(Board board) {
        board.setCreatedDate(LocalDate.now());

        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user.getNickname();
        board.setWriter(nickname);

        boardRepository.save(board);
    }

    //게시글 보기
    public Board getBoard(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        board.setViewCount(board.getViewCount()+1);
        return boardRepository.save(board);
    }

    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

    public void likeBoard(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board id: " + boardId));

        // 중복 추천을 막기 위해 한 회원이 한 게시글에 대해 여러 번 추천하지 못하게 함
        if (boardLikeRepository.findByBoardAndMember(board, member).isPresent()) {
            throw new IllegalStateException("이미 추천한 게시물입니다.");
        }

        // BoardLike 엔티티 생성 및 저장
        BoardLike boardLike = new BoardLike();
        boardLike.setBoard(board);
        boardLike.setMember(member);
        boardLikeRepository.save(boardLike);

        // 해당 게시글(Board)에 대한 추천 수 증가
        board.setLikeCount(board.getLikeCount() + 1);
        boardRepository.save(board);
    }

}
