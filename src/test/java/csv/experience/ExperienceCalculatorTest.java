package csv.experience;

import employees.experience.DateRange;
import employees.experience.EmployeeProjectExperience;
import employees.experience.ExperienceCalculator;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.MONTHS;
import static org.junit.Assert.*;

public class ExperienceCalculatorTest {

    @Test
    public void testOverlappingMonths() {
        var now = LocalDate.now();
        var firstRange = new DateRange(now, now.plus(5, MONTHS));
        var secondRange = new DateRange(now.plus(3, MONTHS), now.plus(8, MONTHS));

        var overlappingMonths = ExperienceCalculator.getOverlappingMonths(firstRange, secondRange);
        assertEquals(2, overlappingMonths);
    }

    @Test
    public void testHasOverlappingMonths() {
        var now = LocalDate.now();
        var dateRangeList = List.of(
                new DateRange(now, now.plus(1, MONTHS)),
                new DateRange(now.plus(2, MONTHS), now.plus(5, MONTHS)),
                new DateRange(now.plus(3, MONTHS), now.plus(8, MONTHS))
        );

        var hasOverlappingMonths = ExperienceCalculator.hasOverlappingMonths(dateRangeList);
        assertTrue(hasOverlappingMonths);
    }

    @Test
    public void testDoesNotHaveOverlappingMonths() {
        var now = LocalDate.now();
        var dateRangeList = List.of(
                new DateRange(now, now.plus(1, MONTHS)),
                new DateRange(now.plus(2, MONTHS), now.plus(5, MONTHS)),
                new DateRange(now.plus(6, MONTHS), now.plus(8, MONTHS))
        );

        var hasOverlappingMonths = ExperienceCalculator.hasOverlappingMonths(dateRangeList);
        assertFalse(hasOverlappingMonths);
    }

    @Test
    public void testTotalOverlappingMonths() {
        var now = LocalDate.now();
        var firstDateRangeList = List.of(
                new DateRange(now, now.plus(1, MONTHS)),
                new DateRange(now.plus(6, MONTHS), now.plus(8, MONTHS))
        );
        var secondDateRangeList = List.of(
                new DateRange(now, now.plus(5, MONTHS)),
                new DateRange(now.plus(7, MONTHS), now.plus(9, MONTHS))
        );

        var firstEmployeeExperience = new EmployeeProjectExperience(1, 2, firstDateRangeList);
        var secondEmployeeExperience = new EmployeeProjectExperience(3, 2, secondDateRangeList);

        var totalOverlappingMonths = ExperienceCalculator.getTotalOverlappingMonths(firstEmployeeExperience,
                secondEmployeeExperience);
        assertEquals(2, totalOverlappingMonths);
    }
}
