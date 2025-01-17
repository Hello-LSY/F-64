package F64.Board;

import F64.Board.Comment.Comment;
import F64.Board.Comment.CommentService;
import F64.User.CustomUser;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final UserSecurityService userSecurityService;

    @GetMapping("/list")
    public String list(Model model, Pageable pageable) {
        Page<Board> boardPage = boardService.getBoardPage(pageable);
        List<Board> boardList = boardPage.getContent(); // Page<Board> → List<Board>

        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = (user != null) ? user.getNickname() : "null";

        model.addAttribute("nickname", nickname);
        model.addAttribute("boardList", boardList); // 기존 boardPage 대신 boardList 추가
        model.addAttribute("boardPage", boardPage); // 페이징을 위해 유지

        return "boardForm";
    }

    @GetMapping("/write")
    public String writeForm(Model model) {
        CustomUser user = userSecurityService.getCurrentUser();
        model.addAttribute("nickname", (user != null) ? user.getNickname() : "null");
        model.addAttribute("board", new Board());
        return "writeForm";
    }

    @PostMapping("/writePro")
    public String write(@ModelAttribute Board board, @RequestParam("imageFile") MultipartFile imageFile) {
        boardService.writeBoard(board, imageFile);
        return "redirect:/board/list";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "updateForm";
    }

    @PostMapping("/updatePro/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Board board, @RequestParam("imageFile") MultipartFile imageFile) {
        boardService.updateBoard(id, board, imageFile);
        return "redirect:/board/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, Authentication authentication) {
        Board board = boardService.getBoardAndIncreaseViewCount(id);
        model.addAttribute("board", board);

        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = (user != null) ? user.getNickname() : "non-login status";
        model.addAttribute("nickname", nickname);

        boolean isWriter = authentication != null && authentication.getName().equals(board.getWriterUsername());
        model.addAttribute("isWriter", isWriter);

        List<Comment> commentList = commentService.getCommentList(id);
        model.addAttribute("commentList", commentList);

        return "boardView";
    }
}
