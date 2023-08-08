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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import F64.User.CustomUser;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final UserSecurityService userSecurityService;
    private final UserRepository userRepository;
    private final DeletedBoardRepository deletedBoardRepository;
    private final CommentRepository commentRepository;
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);


    //게시글 쓰기
    public void writeBoard(Board board, MultipartFile file) {
        board.setCreatedDate(LocalDateTime.now());

        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user.getNickname();
        board.setWriterNickname(nickname);
        board.setLikeCount(0);
        board.setViewCount(0);
        board.setWriterUsername(user.getUsername());

        if(!file.isEmpty() && file != null) {
            try {
                String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";
                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + file.getOriginalFilename();
                File saveFile = new File(filePath, fileName);
                file.transferTo(saveFile);
                board.setFilename(fileName);
                board.setFilepath("/files/" + fileName);
                // 로그 기록
                logger.info("Image file saved at: {}", filePath);
            } catch (IOException e) {
                // 예외 처리
                logger.error("에러발생 :", e);
            }
        }

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
        deletedBoard.setFilename(board.getFilename());
        deletedBoardRepository.save(deletedBoard);



        //댓글도 삭제 추가
        commentRepository.deleteByBoardId(boardId);
        boardLikeRepository.deleteByBoardId(boardId);
        boardRepository.deleteById(boardId);
    }

    public List<Board> getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        Collections.reverse(boardList); // 리스트를 역순으로 정렬
        return boardList;
    }

    public Board getBoardById(Long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. "));
    }

    public void updateBoard(Long id, Board updateBoard, MultipartFile file){
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
        board.setTitle(updateBoard.getTitle());
        board.setContent(updateBoard.getContent());

        if(!file.isEmpty() && file != null) {
            try {
                String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";
                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + file.getOriginalFilename();
                File saveFile = new File(filePath, fileName);
                file.transferTo(saveFile);
                board.setFilename(fileName);
                board.setFilepath("/files/" + fileName);
                // 로그 기록
                logger.info("Image file saved at: {}", filePath);
            } catch (IOException e) {
                // 예외 처리
                logger.error("에러발생 :", e);
            }
        }

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

    public Page<Board> getBoardPage(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }



}


