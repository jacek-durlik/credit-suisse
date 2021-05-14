package pl.jacekdurlik.creditsuisse;

import com.google.gson.Gson;
import lombok.extern.java.Log;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import pl.jacekdurlik.creditsuisse.components.DbWriter;
import pl.jacekdurlik.creditsuisse.components.FileParser;
import pl.jacekdurlik.creditsuisse.domain.Event;
import pl.jacekdurlik.creditsuisse.domain.EventFromLogEntry;
import pl.jacekdurlik.creditsuisse.domain.LogEntry;
import pl.jacekdurlik.creditsuisse.domain.Status;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

@SpringBootApplication
@Log
public class CreditSuisseApplication {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please provide filename as parameter!");
            return;
        }
        ApplicationContext context = SpringApplication.run(CreditSuisseApplication.class, args);
        FileParser fileParser = context.getBean(FileParser.class);
        Pair<Set<LogEntry>, Set<LogEntry>> logEntries = fileParser.readFile(args[0]);
        DbWriter dbWriter = context.getBean(DbWriter.class);
        dbWriter.writeDatabase(logEntries);
    }

}
