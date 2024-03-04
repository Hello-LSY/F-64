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
        Optional<Inquiry> inquiry = inquiryRepository.findById(id);

        if (inquiry.isPresent()) {
            Inquiry actualInquiry = inquiry.get();
            CustomUser user = userSecurityService.getCurrentUser();

            if (user == null) {
                throw new NoSuchElementException("non-login-status");
            }

            String inqNickname = actualInquiry.getNickname();
            String userNickname = user.getNickname();

            boolean isWriter = userNickname.equals(inqNickname) || userNickname.equals("admin");
            boolean isAdmin = userNickname.equals("admin");

            model.addAttribute("inquiry", actualInquiry);
            model.addAttribute("isWriter", isWriter);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("nickname", userNickname);

            return "inquiryDetails";
        } else {
            throw new NoSuchElementException("Inquiry not found");
        }
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
