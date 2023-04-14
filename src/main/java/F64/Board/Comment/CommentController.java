package F64.Board.Comment;

import F64.Board.BoardService;
import F64.User.CustomUser;
import F64.User.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class CommentController {

    @Autowired
    private BoardService boardService;


    @PostMapping("/board/comment/{id}")
    public String addComment(@PathVariable("id") Long boardId, @RequestParam("content") String content, Authentication authentication) {

        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        System.out.println("아이디 확인 " + customUser.getId());
        boardService.saveComment(customUser.getId(), boardId, content);

        return "redirect:/board/view/" + boardId;
    }


    @PostMapping("/board/view/{boardId}/comment/delete/{commentId}")
    public String deleteComment(@PathVariable Long boardId, @PathVariable Long commentId){
        boardService.deleteComment(commentId);
        return "redirect:/board/view/" + boardId;
    }

}
