package csv.experience;

import employees.experience.DateRange;
import employees.experience.EmployeeProjectExperience;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.MONTHS;

public class EmployeeProjectExperienceTest {

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenOverlappingRanges() {
        var now = LocalDate.now();
        var dateRanges = List.of(
                new DateRange(now, now.plus(5, MONTHS)),
                new DateRange(now.plus(3, MONTHS), now.plus(10, MONTHS))
        );

        new EmployeeProjectExperience(1, 2, dateRanges);
    }
}
