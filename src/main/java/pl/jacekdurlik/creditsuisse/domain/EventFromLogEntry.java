package pl.jacekdurlik.creditsuisse.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventFromLogEntry {
    public static Event fromLogEntry(LogEntry startLogEntry, LogEntry finishLogEntry) {
        Long duration = finishLogEntry.getTimestamp() - startLogEntry.getTimestamp();
        return Event.builder()
                .eventId(startLogEntry.getId())
                .host(StringUtils.defaultIfBlank(startLogEntry.getHost(), finishLogEntry.getHost()))
                .type(StringUtils.defaultIfBlank(startLogEntry.getType(), finishLogEntry.getType()))
                .duration(duration)
                .alert(duration > 4)
                .build();
    }
}
