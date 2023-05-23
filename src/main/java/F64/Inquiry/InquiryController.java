package F64.Inquiry;

import F64.User.CustomUser;
import F64.User.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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
        List<Inquiry> inquiries = inquiryRepository.findAll();
        model.addAttribute("inquiries", inquiries);

        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "non-login status";
        model.addAttribute("nickname", nickname);
        return "inquiryListForm";
    }

    @GetMapping("/inquiry/submit")
    public String InquirySubmitForm(){
        return "inquirySubmitForm";
    }

    @PostMapping("/inquiry/submitpro")
    public String InquirySubmitPro(Inquiry inquiry){
        inquiryService.writeInquiry(inquiry);
        return "redirect:/inquiry/list";
    }

    @GetMapping("/inquiry/details/{id}")
    public String showInquiryDetails(@PathVariable("id") Long id, Model model) {
        Optional<Inquiry> inquiry = inquiryRepository.findById(id);

        if (inquiry.isPresent()) {
            model.addAttribute("inquiry", inquiry.get());
            return "inquiryDetails";
        }
        return "redirect:/inquiry/list";
    }

    @PostMapping("/inquiry/answer/{id}")
    public String saveAnswer(@PathVariable("id") Long id, @RequestParam("answer") String answer) {
        inquiryService.saveAnswer(id, answer);
        return "redirect:/inquiry/details/{id}";
    }

}
