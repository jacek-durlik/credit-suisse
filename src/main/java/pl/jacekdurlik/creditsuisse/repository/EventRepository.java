package pl.jacekdurlik.creditsuisse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jacekdurlik.creditsuisse.domain.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
