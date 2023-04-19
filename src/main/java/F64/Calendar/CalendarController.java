package F64.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CalendarController {

    private final CalendarService calendarService;

    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    @Autowired
    public CalendarController(CalendarService calendarService){
        this.calendarService = calendarService;
    }

    @PostMapping("/calendar/event/add")
    public @ResponseBody Event addEvent(@RequestParam String title, @RequestParam String startDate, @RequestParam String endDate) {
        LocalDateTime startDateTime = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME);
        return calendarService.addEvent(title, startDateTime, endDateTime);
    }

    @GetMapping("/calendar")
    public String CalendarForm(Model model){
        List<Event> eventList = calendarService.getAllEvent();
        model.addAttribute("eventList", eventList);
        return "f64Calendar";
    }

    @GetMapping("/calendar/event/all")
    @ResponseBody
    public List<Event> getAllEvent() {
        return calendarService.getAllEvent();
    }

    @DeleteMapping("/calendar/event/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvent(@PathVariable String id) {
        Long eventId = Long.parseLong(id);
        calendarService.deleteEvent(eventId);
        logger.info("Deleted event with id : {}", eventId);
    }




}
