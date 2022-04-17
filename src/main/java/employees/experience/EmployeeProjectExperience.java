package employees.experience;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class EmployeeProjectExperience {
    private static final String EXCEPTION_FORMAT = "Employee %s can not have overlapping employees.experience with themself.";

    private final long employeeId;
    private final long projectId;
    private final List<DateRange> projectExperience;

    public EmployeeProjectExperience(long employeeId, long projectId, List<DateRange> projectExperience) {
        if (ExperienceCalculator.hasOverlappingMonths(projectExperience)) {
            throw new IllegalArgumentException(String.format(EXCEPTION_FORMAT, employeeId));
        }

        this.employeeId = employeeId;
        this.projectId = projectId;
        this.projectExperience =Collections.unmodifiableList(projectExperience);
    }
}
