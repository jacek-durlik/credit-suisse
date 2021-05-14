package pl.jacekdurlik.creditsuisse.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogEntry {
    private String id;
    private Status state;
    private long timestamp;
    private String type;
    private String host;
}
