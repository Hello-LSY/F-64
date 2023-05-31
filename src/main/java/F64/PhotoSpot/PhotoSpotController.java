package F64.PhotoSpot;

import F64.User.CustomUser;
import F64.User.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class PhotoSpotController {

    @Autowired
    private PhotoSpotService photoSpotService;
    @Autowired
    private UserSecurityService userSecurityService;

    @GetMapping("/photospot")
    public String PhotoSpotForm(Model model){
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "null";
        model.addAttribute("nickname", nickname);
        return "photospotForm";
    }

    @PostMapping("/photospot/save")
    public ResponseEntity<String> savePhotoSpot(@RequestBody PhotoSpot photoSpot){
        photoSpotService.savePhotoSpot(photoSpot);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/photospot/view/{id}")
    public String viewPhotoSpot(@PathVariable Long id, Model model, Authentication authentication) {
        PhotoSpot photoSpot = photoSpotService.getPhotoSpotById(id);
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "null";

        boolean isWriter = false;
        if(authentication != null){
            String loginNickname = user.getNickname();
            String photoSpotNickname = photoSpot.getUserNickname();
            if(loginNickname.equals(photoSpotNickname) || loginNickname.equals("admin")){
                isWriter = true;
            }
        }
        else{
            throw new NoSuchElementException("Id not found");
        }

        model.addAttribute("nickname", nickname);
        model.addAttribute("photoSpot", photoSpot);
        model.addAttribute("isWriter", isWriter);

        return "photospotView";
    }

    @GetMapping("/photospot/list")
    public ModelAndView getPhotoSpotList(ModelAndView mav) {
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "null";

        List<PhotoSpot> photoSpotList = photoSpotService.getPhotoSpotList();
        mav.addObject("nickname", nickname);
        mav.addObject("photoSpotList", photoSpotList);
        mav.setViewName("photospotlistForm");
        return mav;
    }

    @PostMapping("/photospot/delete/{id}")
    public String deletePhotoSpot(@PathVariable("id") Long id){
        photoSpotService.deletePhotoSpot(id);
        return "redirect:/photospot/list";
    }

}