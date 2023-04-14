package F64.Board.Like;

import F64.Board.BoardService;
import F64.User.Member;
import F64.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class BoardLikeController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardService boardService;

    @PostMapping("/board/like/{id}")
    public ModelAndView likeBoard(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<Member> optionalMember = userRepository.findByusername(currentUsername);
        if (!optionalMember.isPresent()) {
            throw new IllegalStateException("사용자 정보를 찾을 수 없습니다.");
        }
        Member member = optionalMember.get();

        ModelAndView mav = new ModelAndView();

        boolean isLiked = boardService.likeBoard(id, member);
        if (!isLiked) {
            // 중복 추천 시 예외 처리
            mav.addObject("message", "이미 추천한 게시물입니다.");
            mav.setViewName("alert");
            return mav;
        }

        mav.setViewName("redirect:/board/view/" + id);
        return mav;
    }
}
