package employees.csv;

import lombok.Data;

import java.time.LocalDate;

/**
 * Class representing CSV rows.
 */
@Data
public class CsvEntry {

    private final long employeeId;
    private final long projectId;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
}
