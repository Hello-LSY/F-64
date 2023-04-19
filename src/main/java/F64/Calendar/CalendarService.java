package F64.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CalendarService {


    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }
    public List<Event> getAllEvent(){
        return calendarRepository.findAll();
    }

    public Event addEvent(String title, LocalDateTime startDate, LocalDateTime endDate) {
        Event event = new Event();
        event.setTitle(title);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        return calendarRepository.save(event);
    }


    public void deleteEvent(Long id){
        calendarRepository.deleteById(id);
    }

}
