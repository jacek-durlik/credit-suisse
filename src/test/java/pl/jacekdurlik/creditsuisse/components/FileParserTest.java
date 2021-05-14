package pl.jacekdurlik.creditsuisse.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jacekdurlik.creditsuisse.domain.LogEntry;
import pl.jacekdurlik.creditsuisse.domain.Status;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class FileParserTest {
    @Mock
    private DbWriter dbWriter;

    @InjectMocks
    private FileParser fileParser;

    private final LogEntry expectedFullEntry = LogEntry.builder()
                    .id("scsmbstgra")
                    .state(Status.STARTED)
                    .type("APPLICATION_LOG")
                    .host("12345")
                    .timestamp(1491377495212L)
                    .build();
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileParser = new FileParser(dbWriter);

    }

    @Test
    void parseFullLine() {
        final String line = "{\"id\": \"scsmbstgra\", \"state\": \"STARTED\", \"type\": \"APPLICATION_LOG\", \"host\": \"12345\", \"timestamp\": 1491377495212}";
        assertThatCode(()->{
            final LogEntry logEntry = fileParser.parseLine(line);
            assertThat(logEntry, equalTo(expectedFullEntry));
        }).doesNotThrowAnyException();
    }

    @Test
    void parsePartialLine() {
        final LogEntry expectedEntry = LogEntry.builder()
                .id("scsmbstgrb")
                .state(Status.STARTED)
                .timestamp(1491377495213L)
                .build();
        final String line = "{\"id\": \"scsmbstgrb\", \"state\": \"STARTED\", \"timestamp\": 1491377495213}";
        assertThatCode(()->{
            final LogEntry logEntry = fileParser.parseLine(line);
            assertThat(logEntry, equalTo(expectedEntry));
        }).doesNotThrowAnyException();
    }

    @Test
    void returnsNullWhenParseError() {
        final String line = "this is not a json";
        assertThatCode(()->{
            final LogEntry logEntry = fileParser.parseLine(line);
            assertThat(logEntry, nullValue());
        }).doesNotThrowAnyException();
    }

    @Test
    void returnsNullWhenNullLine() {
        final String line = null;
        assertThatCode(()->{
            final LogEntry logEntry = fileParser.parseLine(line);
            assertThat(logEntry, nullValue());
        }).doesNotThrowAnyException();
    }

    @Test
    void getMatchingEntryMatch() {
        Set<LogEntry> set = new HashSet<>();
        set.add(expectedFullEntry);
        assertThat(FileParser.getMatchingEntry(set, "scsmbstgra"), notNullValue());
    }

    @Test
    void getMatchingEntryNoMatch() {
        Set<LogEntry> set = new HashSet<>();
        set.add(expectedFullEntry);
        assertThat(FileParser.getMatchingEntry(set, "scsmbstgrb"), nullValue());
    }
}