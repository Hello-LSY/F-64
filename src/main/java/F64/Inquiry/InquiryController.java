package F64.Inquiry;

import F64.User.CustomUser;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;
    private final InquiryRepository inquiryRepository;
    private final UserSecurityService userSecurityService;

    /** 문의 리스트 조회 */
    @GetMapping("/list")
    public String showInquiryList(Model model) {
        List<Inquiry> inquiries = inquiryService.getInquiryList();
        CustomUser user = userSecurityService.getCurrentUser();
        model.addAttribute("nickname", user != null ? user.getNickname() : "Guest");
        model.addAttribute("inquiries", inquiries);
        return "inquiryList";
    }

    /** 문의 작성 폼 */
    @GetMapping("/submit")
    public String inquirySubmitForm(Model model) {
        CustomUser user = userSecurityService.getCurrentUser();
        model.addAttribute("nickname", user != null ? user.getNickname() : "Guest");
        return "inquirySubmit";
    }

    /** 문의 등록 */
    @PostMapping("/submitpro")
    public String inquirySubmitPro(@ModelAttribute Inquiry inquiry) {
        CustomUser user = userSecurityService.getCurrentUser();
        if (user == null) {
            return "redirect:/user/login"; // 🔹 로그인하지 않은 경우 로그인 페이지로 리디렉트
        }

        inquiry.setNickname(user.getNickname()); // 작성자 닉네임 추가
        inquiryService.writeInquiry(inquiry);
        return "redirect:/inquiry/list";
    }

    /** 문의 상세 조회 */
    @GetMapping("/details/{id}")
    public String showInquiryDetails(@PathVariable("id") Long id, Model model) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));

        CustomUser user = userSecurityService.getCurrentUser();
        boolean isWriter = user != null && (user.getNickname().equals(inquiry.getNickname()) || "admin".equals(user.getNickname()));
        boolean isAdmin = user != null && "admin".equals(user.getNickname());

        model.addAttribute("inquiry", inquiry);
        model.addAttribute("isWriter", isWriter);
        model.addAttribute("isAdmin", isAdmin);
        return "inquiryDetails";
    }


    /** 관리자 답변 작성 */
    @PostMapping("/answer/{id}")
    public String saveAnswer(@PathVariable("id") Long id, @RequestParam("answer") String answer) {
        inquiryService.saveAnswer(id, answer);
        return "redirect:/inquiry/details/" + id;
    }

    /** 문의 삭제 (동기 방식) */
    @PostMapping("/delete/{id}")
    public String deleteInquiry(@PathVariable("id") Long id) {
        inquiryService.deleteInquiry(id);
        return "redirect:/inquiry/list"; // 🔹 삭제 후 문의 목록 페이지로 이동
    }
}
