package F64.Board;


import F64.Board.Comment.Comment;
import F64.User.CustomUser;
import F64.User.UserRepository;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {


    private final BoardService boardService;
    private final UserSecurityService userSecurityService;

    @GetMapping("/board/list")
    public String BoardForm(Model model, Pageable pageable){
        List<Board> boardList = boardService.getBoardList();
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "null";
        Page<Board> boardPage = boardService.getBoardPage(pageable);


        model.addAttribute("nickname", nickname);
        model.addAttribute("boardList", boardList);
        model.addAttribute("boardPage", boardPage);

        return "boardForm";
    }

    @GetMapping("/board/write")
    public String BoardWrite(Model model){
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "null";
        model.addAttribute("nickname", nickname);
        model.addAttribute("board", new Board());
        return "writeForm";
    }

    @PostMapping("/board/writePro")
    public String BoardWritePro(Board board, @RequestParam("imageFile") MultipartFile imageFile) {

        boardService.writeBoard(board, imageFile);
        return "redirect:/board/list";
    }


    @GetMapping("/board/update/{id}")
    public String BoardUpdate(@PathVariable Long id, Model model){
        Board board = boardService.getBoardById(id);
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "null";
        model.addAttribute("nickname", nickname);
        model.addAttribute("board", board);
        return "updateForm";
    }

    @PostMapping("/board/updatePro/{id}")
    public String BoardUpdatePro(@PathVariable Long id,  @ModelAttribute Board board, @RequestParam("imageFile") MultipartFile imageFile){
        boardService.updateBoard(id, board, imageFile);
        return "redirect:/board/list";
    }

    @GetMapping("/board/view/{id}")
    public String BoardView(@PathVariable Long id, Model model, Authentication authentication){
        Board board = boardService.getBoardAndIncreaseViewCount(id);
        model.addAttribute("board", board);
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "non-login status";
        model.addAttribute("nickname", nickname);
        boolean isWriter = false;
        if(authentication != null){
            String username = authentication.getName();

            if(username.equals(board.getWriterUsername())){
                isWriter = true;
            }
        }
        model.addAttribute("isWriter",isWriter);

        List<Comment> commentList = boardService.getCommentList(id);
        model.addAttribute("commentList", commentList);

        boolean isCommentWriter = false;
        for (Comment comment : commentList) {
            if (authentication != null) {
                String username = authentication.getName();
                if (username.equals(comment.getWriterUsername())) {
                    isCommentWriter = true;
                }
            }
        }
        model.addAttribute("isCommentWriter", isCommentWriter);


        return "boardView";
    }


}