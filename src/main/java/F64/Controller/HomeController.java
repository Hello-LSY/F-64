package F64.Controller;

import F64.Board.Board;
import F64.Board.BoardService;
import F64.User.CustomUser;
import F64.User.Member;
import F64.User.UserRepository;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class HomeController {

    @Autowired
    private final BoardService boardService;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserSecurityService userSecurityService;
    @GetMapping("/")
    public String homeForm(Model model) {
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "non-login status";
        model.addAttribute("nickname", nickname);

        List<Board> latestBoardList = boardService.getLatestBoardList();
        model.addAttribute("BoardList", latestBoardList);
        return "homepage";
    }



}