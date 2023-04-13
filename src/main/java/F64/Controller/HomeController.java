package F64.Controller;

import F64.User.CustomUser;
import F64.User.Member;
import F64.User.UserRepository;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final UserSecurityService userSecurityService;
    @GetMapping("/")
    public String homeForm(Model model) {
        model.addAttribute("title", "사진 동아리 홈페이지");
        model.addAttribute("description", "순간을 담아 영원히");

        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "non-login status";
        model.addAttribute("nickname", nickname);

        return "homepage";
    }


}