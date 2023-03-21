package F64.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homeForm(Model model) {
        model.addAttribute("title", "사진 동아리 홈페이지");
        model.addAttribute("description", "순간을 담아 영원히");
        return "homepage";
    }

}