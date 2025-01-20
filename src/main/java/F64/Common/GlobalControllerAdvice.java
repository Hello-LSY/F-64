package F64.Common;

import F64.User.CustomUser;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserSecurityService userSecurityService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        CustomUser user = userSecurityService.getCurrentUser();
        model.addAttribute("nickname", user != null ? user.getNickname() : "Guest");
    }
}
