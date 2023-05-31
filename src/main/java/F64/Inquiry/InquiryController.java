package F64.Inquiry;

import F64.User.CustomUser;
import F64.User.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class InquiryController {


    @Autowired
    private InquiryRepository inquiryRepository;
    @Autowired
    private InquiryService inquiryService;
    @Autowired
    private UserSecurityService userSecurityService;

    @GetMapping("/inquiry/list")
    public String showInquiryList(Model model) {
        List<Inquiry> inquiries = inquiryService.getInquiryList();


        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "non-login status";
        model.addAttribute("nickname", nickname);
        model.addAttribute("inquiries", inquiries);
        return "inquiryListForm";
    }

    @GetMapping("/inquiry/submit")
    public String InquirySubmitForm(Model model){
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "null";
        model.addAttribute("nickname", nickname);
        return "inquirySubmitForm";
    }

    @PostMapping("/inquiry/submitpro")
    public String InquirySubmitPro(Inquiry inquiry){
        inquiryService.writeInquiry(inquiry);
        return "redirect:/inquiry/list";
    }

    @GetMapping("/inquiry/details/{id}")
    public String showInquiryDetails(@PathVariable("id") Long id, Model model, Authentication authentication) {
        //null 대비. Optional말고 .orElseThrow로해도됨
        Optional<Inquiry> inquiry = inquiryRepository.findById(id);
        boolean isWriter = false;
        boolean isAdmin = false;
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "null";

        if (inquiry.isPresent()) {
            Inquiry actualInquiry = inquiry.get();
            String inqNickname = actualInquiry.getNickname();
            String userNickname = user.getNickname();
            if(authentication != null){
                if (userNickname.equals(inqNickname) || userNickname.equals("admin")) {
                    isWriter = true;
                }
                if (userNickname.equals("admin")) {
                    isAdmin = true;
                }
            }else {
                throw new NoSuchElementException("non-login-status");
            }
            model.addAttribute("inquiry", actualInquiry);
            model.addAttribute("isWriter",isWriter);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("nickname", nickname);
        }
        else {
            throw new NoSuchElementException("Inquiry not found");
        }
        return "inquiryDetails";
    }

    @PostMapping("/inquiry/answer/{id}")
    public String saveAnswer(@PathVariable("id") Long id, @RequestParam("answer") String answer) {
        inquiryService.saveAnswer(id, answer);
        return "redirect:/inquiry/details/{id}";
    }

    @PostMapping("/inquiry/delete/{id}")
    public String deleteInquiry(@PathVariable("id") Long id){
        inquiryService.deleteInquiry(id);
        return "redirect:/inquiry/list";
    }

}
