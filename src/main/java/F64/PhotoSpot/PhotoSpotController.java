package F64.PhotoSpot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PhotoSpotController {

    @Autowired
    private PhotoSpotService photoSpotService;

    @GetMapping("/photospot")
    public String PhotoSpotForm(){
        return "photospotForm";
    }

    @PostMapping("/photospot/save")
    public ResponseEntity<String> savePhotoSpot(@RequestBody PhotoSpot photoSpot){
        photoSpotService.savePhotoSpot(photoSpot);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/photospot/{id}")
    public String viewPhotoSpot(@PathVariable Long id, Model model) {
        PhotoSpot photoSpot = photoSpotService.getPhotoSpotById(id);
        model.addAttribute("photoSpot", photoSpot);
        return "photospotView";
    }

    @GetMapping("/photospot/list")
    public ModelAndView getPhotoSpotList(ModelAndView mav) {
        List<PhotoSpot> photoSpotList = photoSpotService.getPhotoSpotList();
        mav.addObject("photoSpotList", photoSpotList);
        mav.setViewName("photospotlistForm");
        return mav;
    }


}
