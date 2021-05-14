package pl.jacekdurlik.creditsuisse.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jacekdurlik.creditsuisse.domain.Event;
import pl.jacekdurlik.creditsuisse.domain.LogEntry;
import pl.jacekdurlik.creditsuisse.domain.Status;
import pl.jacekdurlik.creditsuisse.service.EventService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DbWriterTest {
    @Mock
    private EventService eventService;

    @InjectMocks
    private DbWriter dbWriter;

    private final Event savedEvent = Event.builder()
            .eventId("scsmbstgra")
            .alert(Boolean.TRUE)
            .type("APPLICATION_LOG")
            .host("12345")
            .duration(5L)
            .build();

    private final LogEntry start = LogEntry.builder()
            .id("scsmbstgra")
            .timestamp(1L)
            .type("APPLICATION_LOG")
            .host("12345")
            .state(Status.STARTED)
            .build();

    private final LogEntry finish = LogEntry.builder()
            .id("scsmbstgra")
            .timestamp(6L)
            .type("APPLICATION_LOG")
            .host("12345")
            .state(Status.FINISHED)
            .build();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dbWriter = new DbWriter(eventService);
        when(eventService.save(any())).thenReturn(savedEvent);
    }

    @Test
    void saveEntry() {
        dbWriter.saveEntry(start, finish);

        verify(eventService).save(eq(savedEvent));
    }
}