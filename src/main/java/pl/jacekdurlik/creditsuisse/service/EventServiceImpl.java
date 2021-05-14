package pl.jacekdurlik.creditsuisse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jacekdurlik.creditsuisse.domain.Event;
import pl.jacekdurlik.creditsuisse.repository.EventRepository;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public Event save(Event e) {
        return eventRepository.save(e);
    }
}
