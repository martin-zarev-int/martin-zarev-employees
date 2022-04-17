package employees.services;

import employees.csv.CsvEntry;
import employees.experience.DateRange;
import employees.experience.EmployeeProjectExperience;
import employees.experience.ExperienceCalculator;
import employees.experience.PairExperience;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExperienceService {

    /**
     * Extracts all {@link PairExperience} objects which have the provided pair.
     */
    public static List<PairExperience> extractPairsWithTheSameEmployees(List<PairExperience> allPairs,
                                                                        PairExperience searchedPair) {
        return allPairs.stream()
                .filter(p -> isWithTheSameEmployees(p, searchedPair))
                .collect(toList());
    }

    /**
     * Finds the pair which have worked together the most.
     */
    public static Optional<PairExperience> getPairWithTheMostOverlappingExperience(List<PairExperience> pairList) {
        return pairList.stream().max(comparing(PairExperience::getOverlappingMonths));
    }

    /**
     * Retrieves a list of {@link PairExperience} for each pair of employees that have worked on the same project.
     */
    public static List<PairExperience> getPairExperienceList(List<CsvEntry> csvEntries) {
        var groupedCsvEntries = csvEntries.stream().collect(groupingBy(CsvEntry::getProjectId));

        var employeesExperienceByProject = groupedCsvEntries.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> getEmployeesExperienceList(e.getKey(), e.getValue())));

        return employeesExperienceByProject.entrySet().stream()
                .map(e -> getPairExperienceListForProject(e.getKey(), e.getValue()))
                .flatMap(Collection::stream)
                .collect(toList());
    }

    /**
     * Checks if the {@link PairExperience} objects have the same employees.
     */
    private static boolean isWithTheSameEmployees(PairExperience firstPair, PairExperience secondPair) {
        return (firstPair.getFirstEmployeeId() == secondPair.getFirstEmployeeId()
                        && firstPair.getSecondEmployeeId() == secondPair.getSecondEmployeeId())
                ||
                (firstPair.getFirstEmployeeId() == secondPair.getSecondEmployeeId()
                        && firstPair.getSecondEmployeeId() == secondPair.getFirstEmployeeId());
    }

    /**
     * Creates a {@link PairExperience} object for all pairs of employees which have worked on the provided project.
     */
    private static List<PairExperience> getPairExperienceListForProject(
            long projectId,
            List<EmployeeProjectExperience> employeesExperienceList
    ) {
        List<PairExperience> pairExperienceList = new ArrayList<>();
        for (int i = 0; i < employeesExperienceList.size() - 1; i++) {
            for(int j = i + 1; j < employeesExperienceList.size(); j++) {
                pairExperienceList.add(generatePairExperience(projectId, employeesExperienceList.get(i),
                        employeesExperienceList.get(j)));
            }
        }

        return pairExperienceList;
    }

    /**
     * Calculates the overlapping months of both employees and creates a {@link PairExperience} object.
     */
    private static PairExperience generatePairExperience(long projectId,
                                                        EmployeeProjectExperience firstEmployeeExperience,
                                                        EmployeeProjectExperience secondEmployeeExperience) {
        var overlappingMonths = ExperienceCalculator.getTotalOverlappingMonths(firstEmployeeExperience,
                secondEmployeeExperience);
        return new PairExperience(firstEmployeeExperience.getEmployeeId(), secondEmployeeExperience.getEmployeeId(),
                projectId, overlappingMonths);
    }

    /**
     * Creates an {@link EmployeeProjectExperience} object holding the experience of each employee for a given project.
     * @param projectId The ID of the project.
     * @param entries All the entries for a specific project.
     * @return {@link EmployeeProjectExperience} containing one entry for each employee.
     */
    private static List<EmployeeProjectExperience> getEmployeesExperienceList(long projectId, List<CsvEntry> entries) {
        var employeeGroupedEntries = entries.stream().collect(groupingBy(CsvEntry::getEmployeeId));

        return employeeGroupedEntries.entrySet().stream()
                .map(e -> new EmployeeProjectExperience(e.getKey(), projectId, getEmployeeDateRanges(e.getValue())))
                .collect(toList());
    }

    /**
     * Creates a date range from each employee entry(csv row).
     * @param employeeCsvEntries All the rows for an employee in a given project.
     */
    private static List<DateRange> getEmployeeDateRanges(List<CsvEntry> employeeCsvEntries) {
        List<DateRange> employeeExperienceDateRanges = new ArrayList<>();
        for (var csvEntry : employeeCsvEntries) {
            employeeExperienceDateRanges.add(new DateRange(csvEntry.getDateFrom(), csvEntry.getDateTo()));
        }

        return employeeExperienceDateRanges;
    }
}
