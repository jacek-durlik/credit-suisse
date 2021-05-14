package pl.jacekdurlik.creditsuisse.components;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import pl.jacekdurlik.creditsuisse.domain.LogEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Component
@Log
@RequiredArgsConstructor
public class FileParser {
    private final DbWriter dbWriter;
    private final Gson gson = new Gson();

    public Pair<Set<LogEntry>, Set<LogEntry>> readFile(String filename) {
        File myObj = new File(filename);
        try {
            Scanner scanner = new Scanner(myObj);
            Set<LogEntry> entrySetStarted = new HashSet<>();
            Set<LogEntry> entrySetFinished = new HashSet<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                LogEntry logEntry = parseLine(line);
                if (Objects.nonNull(logEntry) && Objects.nonNull(logEntry.getState())) {
                    switch (logEntry.getState()) {
                        case STARTED:
                            LogEntry finished = getMatchingEntry(entrySetFinished, logEntry.getId());
                            if (Objects.nonNull(finished)) {
                                dbWriter.saveEntry(logEntry, finished);
                                entrySetFinished.remove(finished);
                            } else {
                                entrySetStarted.add(logEntry);
                            }
                            break;
                        case FINISHED:
                            LogEntry started = getMatchingEntry(entrySetStarted, logEntry.getId());
                            if (Objects.nonNull(started)) {
                                dbWriter.saveEntry(started, logEntry);
                                entrySetStarted.remove(started);
                            } else {
                                entrySetFinished.add(logEntry);
                            }
                            break;
                        default:
                            log.warning("Unrecognized status, entry discarded.");
                            break;
                    }
                } else {
                    log.warning("Object is null, probably parsing error.");
                }
            }
            log.finer("Parsing finished");
            return Pair.of(entrySetStarted, entrySetFinished);
        } catch (FileNotFoundException e) {
            log.severe("File not found!");
            return Pair.of(Collections.emptySet(), Collections.emptySet());
        }
    }

    public LogEntry parseLine(String line) {
        log.finer("Parsing line: " + line);
        try {
            LogEntry logEntry = gson.fromJson(line, LogEntry.class);
            log.finer("Parsed object: " + Objects.toString(logEntry));
            return logEntry;
        } catch (JsonSyntaxException ex) {
            log.warning("Parsing error");
            return null;
        }
    }

    public static LogEntry getMatchingEntry(Set<LogEntry> set, String id) {
        return set.parallelStream().filter(fn->fn.getId().equals(id)).findFirst().orElse(null);
    }
}