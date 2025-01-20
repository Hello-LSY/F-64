package F64.Calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    public List<Event> getAllEvent() {
        return calendarRepository.findAll();
    }

    public Event addEvent(String title, LocalDateTime startDate, LocalDateTime endDate, boolean allDay) {
        Event event = new Event();
        event.setTitle(title);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setAllDay(allDay);
        return calendarRepository.save(event);
    }

    public void deleteEvent(Long id) {
        calendarRepository.deleteById(id);
    }

    public List<Event> getEventList() {
        return calendarRepository.findAll();
    }

    public List<Event> getAllDayEventsOrderByStartDateDesc() {
        return calendarRepository.findByAllDayTrueOrderByStartDateDesc();
    }
}
