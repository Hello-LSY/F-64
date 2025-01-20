package F64.Home;

import F64.Board.Board;
import F64.Board.BoardService;
import F64.Calendar.CalendarService;
import F64.Calendar.Event;
import F64.Inquiry.Inquiry;
import F64.Inquiry.InquiryService;
import F64.PhotoSpot.PhotoSpot;
import F64.PhotoSpot.PhotoSpotService;
import F64.User.CustomUser;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final UserSecurityService userSecurityService;
    private final InquiryService inquiryService;
    private final PhotoSpotService photoSpotService;
    private final BoardService boardService;
    private final CalendarService calendarService;

    @GetMapping("/")
    public String homeForm(Model model) {

        // 게시판 데이터 추가
        model.addAttribute("boardList", boardService.getBoardList());
        model.addAttribute("inquiryList", inquiryService.getInquiryList());
        model.addAttribute("photoSpotList", photoSpotService.getPhotoSpotList());
        model.addAttribute("eventList", calendarService.getAllDayEventsOrderByStartDateDesc());

        return "homepage";
    }
}
