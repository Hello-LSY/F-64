package F64.PhotoSpot;

import F64.User.CustomUser;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/photospot")
public class PhotoSpotController {

    private final PhotoSpotService photoSpotService;
    private final UserSecurityService userSecurityService;

    @GetMapping
    public String PhotoSpotForm(Model model) {
//        CustomUser user = userSecurityService.getCurrentUser();
//        model.addAttribute("nickname", user != null ? user.getNickname() : "Guest");
        return "photospotForm";
    }

    @PostMapping("/save")
    @ResponseBody
    public String savePhotoSpot(@RequestBody PhotoSpot photoSpot) {
        CustomUser user = userSecurityService.getCurrentUser();
        if (user != null) {
            photoSpot.setUserNickname(user.getNickname());
        }
        photoSpotService.savePhotoSpot(photoSpot);
        return "success";
    }

    @GetMapping("/view/{id}")
    public String viewPhotoSpot(@PathVariable Long id, Model model) {
        PhotoSpot photoSpot = photoSpotService.getPhotoSpotById(id);
        CustomUser user = userSecurityService.getCurrentUser();

        boolean isWriter = false;
        String nickname = "Guest";

        if (user != null) {
//            nickname = user.getNickname();
            if (user.getNickname().equals(photoSpot.getUserNickname()) || user.getNickname().equals("admin")) {
                isWriter = true;
            }
        }

        model.addAttribute("photoSpot", photoSpot);
        model.addAttribute("isWriter", isWriter);
//        model.addAttribute("nickname", nickname);

        return "photospotView";
    }

    @GetMapping("/list")
    public String getPhotoSpotList(Model model) {
        CustomUser user = userSecurityService.getCurrentUser();
        model.addAttribute("nickname", user != null ? user.getNickname() : "Guest");
        model.addAttribute("photoSpotList", photoSpotService.getPhotoSpotList());
        return "photospotListForm";
    }

    @PostMapping("/delete/{id}")
    public String deletePhotoSpot(@PathVariable("id") Long id) {
        photoSpotService.deletePhotoSpot(id);
        return "redirect:/photospot/list";
    }
}
