package F64.Calendar;

import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);
    private final CalendarService calendarService;
    private final UserSecurityService userSecurityService;

    private LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr.replace("Z", ""), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    }

    @PostMapping("/event/add")
    public @ResponseBody Event addEvent(@RequestParam String title,
                                        @RequestParam String startDate,
                                        @RequestParam String endDate,
                                        @RequestParam(defaultValue = "false") boolean allDay) {
        LocalDateTime startDateTime = parseDateTime(startDate);
        LocalDateTime endDateTime = parseDateTime(endDate);
        return calendarService.addEvent(title, startDateTime, endDateTime, allDay);
    }

    @GetMapping("/event/all")
    @ResponseBody
    public List<Event> getAllEvent() {
        return calendarService.getAllEvent();
    }

    @DeleteMapping("/event/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvent(@PathVariable Long id) {
        calendarService.deleteEvent(id);
        logger.info("Deleted event with ID: {}", id);
    }

    @GetMapping
    public String CalendarForm(Model model) {
        return "f64Calendar";
    }
}
