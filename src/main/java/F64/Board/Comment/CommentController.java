package F64.Board.Comment;

import F64.User.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{id}")
    public String addComment(@PathVariable("id") Long boardId, @RequestParam("content") String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("비로그인 사용자가 댓글을 작성하려고 시도함.");
            return "redirect:/login";  // 로그인 페이지로 리디렉션
        }

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        commentService.saveComment(customUser.getId(), boardId, content);

        return "redirect:/board/view/" + boardId;
    }

    @PostMapping("/view/{boardId}/comment/delete/{commentId}")
    public String deleteComment(@PathVariable Long boardId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/board/view/" + boardId;
    }
}
