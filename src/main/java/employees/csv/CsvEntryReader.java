package employees.csv;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static employees.csv.FieldPositions.*;

public class CsvEntryReader {
    private static final String NULL_ENTRY = "NULL";

    private final DateTimeFormatter dateTimeFormatter;
    private final LocalDate currentLocalDate;

    public CsvEntryReader(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.currentLocalDate = LocalDate.now();
    }

    public List<CsvEntry> getCsvEntries(Reader reader) throws IOException {
        try (CSVReader csvReader = new CSVReader(reader)) {

            return extractEntries(csvReader);
        }
    }

    private List<CsvEntry> extractEntries(CSVReader csvReader) {
        List<CsvEntry> csvEntries = new ArrayList<>();

        for(var line : csvReader) {
            csvEntries.add(extractEntry(line));
        }

        return csvEntries;
    }

    private CsvEntry extractEntry(String[] line) {
        var employeeId = extractLong(line[EMPLOYEE_ID.getPosition()]);
        var projectId = extractLong(line[PROJECT_ID.getPosition()]);

        var dateFrom = extractDate(line[DATE_FROM.getPosition()]);
        var dateTo = extractDate(line[DATE_TO.getPosition()]);
        return new CsvEntry(employeeId, projectId, dateFrom, dateTo);
    }

    private LocalDate extractDate(String stringRepresentation) {
        var trimmedRepresentation = stringRepresentation.trim();

        if(NULL_ENTRY.equals(trimmedRepresentation)) {
            return currentLocalDate;
        }

        return LocalDate.parse(trimmedRepresentation, dateTimeFormatter);
    }

    private static long extractLong(String stringRepresentation) {
        return Long.parseLong(stringRepresentation.trim());
    }
}
