package F64.Inquiry;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InquiryController {

    @GetMapping("/inquiry/list")
    public String InquiryForm(){
        return "inquiryForm";
    }

    @PostMapping("/inquiry/submit")
    public String InquirySubmitForm(){
        return "inquirySubmitForm";
    }

    @PostMapping("/inquiry/submitpro")
    public String InquirySubmitPro(Inquiry inquiry){


        return "redirect:/inquiry/list";
    }

}
