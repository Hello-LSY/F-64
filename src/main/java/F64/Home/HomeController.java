package F64.Home;

import F64.Board.Board;
import F64.Board.BoardService;
import F64.Calendar.CalendarService;
import F64.Calendar.Event;
import F64.Inquiry.Inquiry;
import F64.Inquiry.InquiryRepository;
import F64.Inquiry.InquiryService;
import F64.PhotoSpot.PhotoSpot;
import F64.PhotoSpot.PhotoSpotRepository;
import F64.PhotoSpot.PhotoSpotService;
import F64.User.CustomUser;
import F64.User.Member;
import F64.User.UserRepository;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class HomeController {

    @Autowired
    private UserSecurityService userSecurityService;
    @Autowired
    private InquiryService inquiryService;
    @Autowired
    private PhotoSpotService photoSpotService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private CalendarService calendarService;

    @GetMapping("/")
    public String homeForm(Model model) {
        //닉네임
        CustomUser user = userSecurityService.getCurrentUser();
        String nickname = user != null ? user.getNickname() : "null";
        model.addAttribute("nickname", nickname);
        //자유게시판
        List<Board> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);
        //문의게시판
        List<Inquiry> inquiryList = inquiryService.getInquiryList();
        model.addAttribute("inquiryList", inquiryList);
        //출사신청게시판
        List<PhotoSpot> photoSpotList = photoSpotService.getPhotoSpotList();
        model.addAttribute("photoSpotList", photoSpotList);
        //이벤트 일정
        List<Event> eventList = calendarService.getAlldayEventsOrderByStartDateDesc();
        model.addAttribute("eventList", eventList);

        return "homepage";
    }


}