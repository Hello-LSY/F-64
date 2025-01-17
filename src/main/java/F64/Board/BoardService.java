package F64.Board;

import F64.Board.Comment.Comment;
import F64.Board.Comment.CommentRepository;
import F64.Board.Deleted.DeletedBoard;
import F64.Board.Deleted.DeletedBoardRepository;
import F64.Board.Like.BoardLike;
import F64.Board.Like.BoardLikeRepository;
import F64.User.CustomUser;
import F64.User.Member;
import F64.User.UserRepository;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final UserSecurityService userSecurityService;
    private final CommentRepository commentRepository;
    private final FileService fileService;
    private final DeletedBoardRepository deletedBoardRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    public void writeBoard(Board board, MultipartFile file) {
        board.setCreatedDate(LocalDateTime.now());
        CustomUser user = userSecurityService.getCurrentUser();
        board.setWriterNickname(user.getNickname());
        board.setWriterUsername(user.getUsername());
        board.setLikeCount(0);
        board.setViewCount(0);

        if (!file.isEmpty()) {
            fileService.saveFile(board, file);
        }

        boardRepository.save(board);
        logger.info("게시글 저장 완료 - ID: {}, 제목: {}", board.getId(), board.getTitle());
    }


    @Transactional
    public Board getBoardAndIncreaseViewCount(Long id) {
        boardRepository.increaseViewCount(id);
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
    }

    public void updateBoard(Long id, Board updateBoard, MultipartFile file) {
        Board board = getBoardById(id);
        board.setTitle(updateBoard.getTitle());
        board.setContent(updateBoard.getContent());

        if (!file.isEmpty()) {
            fileService.saveFile(board, file);
        }

        boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (deletedBoardRepository.existsByBoardId(boardId)) {
            throw new IllegalStateException("이미 삭제된 게시글입니다.");
        }

        // DeletedBoard에 데이터를 먼저 저장 (Board 삭제 전에)
        DeletedBoard deletedBoard = DeletedBoard.builder()
                .boardId(boardId) // FK가 아닌 ID 값만 저장
                .title(board.getTitle())
                .content(board.getContent())
                .writerNickname(board.getWriterNickname())
                .writerUsername(board.getWriterUsername())
                .createdDate(board.getCreatedDate())
                .likeCount(board.getLikeCount())
                .viewCount(board.getViewCount())
                .filename(board.getFilename())
                .build();

        deletedBoardRepository.save(deletedBoard);
        logger.info("DeletedBoard에 데이터 저장 완료 - ID: {}", boardId);

        // 연관된 데이터 먼저 삭제
        boardLikeRepository.deleteByBoardId(boardId);
        commentRepository.deleteByBoardId(boardId);
        logger.info("BoardLike & Comment 삭제 완료 - ID: {}", boardId);

        // Board 삭제
        boardRepository.deleteByBoardId(boardId);
        logger.info("Board 삭제 완료 - ID: {}", boardId);

        boolean exists = boardRepository.existsById(boardId);
        logger.info("삭제 요청 후 Board 테이블 확인 - 존재 여부: {}", exists);
    }





    public Page<Board> getBoardPage(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAll(pageable);
        logger.info("조회된 게시글 개수: {}", boardPage.getTotalElements());
        return boardPage;
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
    }

    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

}
