package employees.experience;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DateRange {

    private final LocalDate fromDate;
    private final LocalDate toDate;
}
