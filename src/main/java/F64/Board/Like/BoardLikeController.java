package F64.Board.Like;

import F64.User.Member;
import F64.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardLikeController {

    private final UserRepository userRepository;
    private final BoardLikeService boardLikeService;

    @PostMapping("/board/like/{id}")
    public String likeBoard(@PathVariable("id") Long boardId, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("비로그인 사용자가 좋아요 시도");
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        String currentUsername = authentication.getName();
        Optional<Member> optionalMember = userRepository.findByUsername(currentUsername);

        if (!optionalMember.isPresent()) {
            log.error("사용자 정보 조회 실패: {}", currentUsername);
            redirectAttributes.addFlashAttribute("error", "사용자 정보를 찾을 수 없습니다.");
            return "redirect:/board/view/" + boardId;
        }

        Member member = optionalMember.get();
        boolean isLiked = boardLikeService.likeBoard(boardId, member);

        if (!isLiked) {
            log.warn("중복 추천 방지 - 사용자: {}", member.getUsername());
            redirectAttributes.addFlashAttribute("error", "이미 추천한 게시물입니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "추천이 완료되었습니다.");
        }

        return "redirect:/board/view/" + boardId;
    }
}
