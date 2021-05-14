package pl.jacekdurlik.creditsuisse.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import pl.jacekdurlik.creditsuisse.domain.Event;
import pl.jacekdurlik.creditsuisse.domain.EventFromLogEntry;
import pl.jacekdurlik.creditsuisse.domain.LogEntry;
import pl.jacekdurlik.creditsuisse.service.EventService;

import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Log
public class DbWriter {
    private final EventService eventService;

    public void writeDatabase(Pair<Set<LogEntry>, Set<LogEntry>> logEntries) {
        final Set<LogEntry> entrySetStarted = logEntries.getLeft();
        final Set<LogEntry> entrySetFinished = logEntries.getRight();
        entrySetStarted.forEach(el->{
            LogEntry finished = FileParser.getMatchingEntry(entrySetFinished, el.getId());
            if (Objects.nonNull(finished)) {
                saveEntry(el, finished);
            } else {
                log.info("Matching finished event not found, discarding entry.");
            }

        });
    }

    public void saveEntry(LogEntry started, LogEntry finished) {
        log.finer("Found matching finished event");
        Event event = EventFromLogEntry.fromLogEntry(started, finished);
        Event saved = eventService.save(event);
        log.finer("Saved event to db: "+saved.toString());
    }
}
