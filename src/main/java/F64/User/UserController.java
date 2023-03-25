package F64.User;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;



    @GetMapping("/signup")
    public String loginForm(UserCreateForm userCreateForm) {
        return "signupForm";
    }

    @PostMapping("/signup")
    public String login(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signupForm";
        }
        if (!userCreateForm.getPassword().equals(userCreateForm.getConfirm_password())) {
            bindingResult.rejectValue("confirm_password", "passwordInCorrect", "두 패스워드가 일치하지않습니다.");
            return "signupForm";
        }
        try {
            userService.create(userCreateForm.getUsername(),
                     userCreateForm.getPassword() ,userCreateForm.getNickname());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signupForm";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signupForm";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }
}



