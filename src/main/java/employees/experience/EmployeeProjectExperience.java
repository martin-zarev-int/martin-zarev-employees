package employees.experience;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Used for storing an employee's experience for a given project.
 * {@link EmployeeProjectExperience#projectExperience} List is needed, as employees can be brought back to the same
 * project multiple times.
 */
@Getter
public class EmployeeProjectExperience {
    private static final String EXCEPTION_FORMAT = "Employee %s can not have overlapping experience with themself.";

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
