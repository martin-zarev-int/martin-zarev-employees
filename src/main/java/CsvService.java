import csv.CsvEntry;
import csv.CsvEntryReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class CsvService {

    public static List<CsvEntry> retrieveCsvEntries(Path path, DateTimeFormatter dateTimeFormatter) {
        var csvEntryReader = new CsvEntryReader(DateTimeFormatter.ISO_DATE, path);

        try {
            return csvEntryReader.getCsvEntries();
        } catch (IOException ioException) {
            log.error("System was not able to retrieve csv data.");
        }

        return List.of();
    }
}
