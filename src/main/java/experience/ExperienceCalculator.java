package experience;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExperienceCalculator {

    public static long getTotalOverlappingMonths(EmployeeProjectExperience firstEmployeeExperience,
                                                EmployeeProjectExperience secondEmployeeExperience) {
        long totalExperience = 0;

        for (var firstEmployeeDateRange : firstEmployeeExperience.getProjectExperience()) {
            for (var secondEmployeeDateRange : secondEmployeeExperience.getProjectExperience()) {
                totalExperience += getOverlappingMonths(firstEmployeeDateRange, secondEmployeeDateRange);
            }
        }


        return totalExperience;
    }

    public static boolean hasOverlappingMonths(List<DateRange> dateRangeList) {
        for (int i = 0; i < dateRangeList.size() - 1; i++) {
            for (int j = i + 1; j < dateRangeList.size(); j++) {
                if (getOverlappingMonths(dateRangeList.get(i), dateRangeList.get(j)) != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public static long getOverlappingMonths(DateRange firstRange, DateRange secondRange) {
        var areOverlapping = firstRange.getFromDate().isBefore(secondRange.getToDate())
                && secondRange.getFromDate().isBefore(firstRange.getToDate());
        if (!areOverlapping) {
            return 0;
        }

        var maxFromDate = getMax(firstRange.getFromDate(), secondRange.getFromDate());
        var minToDate = getMin(firstRange.getToDate(), secondRange.getToDate());

        return Period.between(maxFromDate, minToDate).toTotalMonths();
    }

    private static LocalDate getMax(LocalDate firstLocalDate, LocalDate secondLocalDate) {
        if (firstLocalDate.isBefore(secondLocalDate)) {
            return secondLocalDate;
        }

        return firstLocalDate;
    }

    private static LocalDate getMin(LocalDate firstLocalDate, LocalDate secondLocalDate) {
        if (firstLocalDate.isAfter(secondLocalDate)) {
            return secondLocalDate;
        }

        return firstLocalDate;
    }
}
