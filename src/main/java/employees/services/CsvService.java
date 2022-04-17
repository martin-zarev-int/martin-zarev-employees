package employees.services;

import employees.csv.CsvEntry;
import employees.csv.CsvEntryReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class CsvService {

    public static List<CsvEntry> retrieveCsvEntries(Path path, DateTimeFormatter dateTimeFormatter) {
        var csvEntryReader = new CsvEntryReader(dateTimeFormatter);

        try (var reader = Files.newBufferedReader(path)) {
            return csvEntryReader.getCsvEntries(reader);
        } catch (IOException ioException) {
            log.error("System was not able to retrieve employees.csv data.");
        }

        return List.of();
    }

    public static List<CsvEntry> retrieveCsvEntries(MultipartFile file, DateTimeFormatter dateTimeFormatter) {
        var csvEntryReader = new CsvEntryReader(dateTimeFormatter);

        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return csvEntryReader.getCsvEntries(reader);
        } catch (IOException ioException) {
            log.error("System was not able to retrieve employees.csv data.");
        }

        return List.of();
    }
}
