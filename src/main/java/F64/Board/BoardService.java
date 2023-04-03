package F64.Board;

import F64.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    //게시글 쓰기
    public void write(Board board) {
        board.setCreatedDate(LocalDate.now());
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
}
