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

    public static List<PairExperience> extractPairsWithTheSameEmployees(List<PairExperience> allPairs,
                                                                        PairExperience searchedPair) {
        return allPairs.stream()
                .filter(p -> isWithTheSameEmployees(p, searchedPair))
                .collect(toList());
    }

    public static Optional<PairExperience> getPairWithTheMostOverlappingExperience(List<PairExperience> pairList) {
        return pairList.stream().max(comparing(PairExperience::getOverlappingMonths));
    }

    public static List<PairExperience> getPairExperienceList(List<CsvEntry> csvEntries) {
        var groupedCsvEntries = csvEntries.stream().collect(groupingBy(CsvEntry::getProjectId));

        var employeesExperienceByProject = groupedCsvEntries.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> getEmployeesExperienceList(e.getKey(), e.getValue())));

        return employeesExperienceByProject.entrySet().stream()
                .map(e -> getPairExperienceListForProject(e.getKey(), e.getValue()))
                .flatMap(Collection::stream)
                .collect(toList());
    }


    private static boolean isWithTheSameEmployees(PairExperience firstPair, PairExperience secondPair) {
        return (firstPair.getFirstEmployeeId() == secondPair.getFirstEmployeeId()
                && firstPair.getSecondEmployeeId() == secondPair.getSecondEmployeeId())
                ||
                (firstPair.getFirstEmployeeId() == secondPair.getSecondEmployeeId()
                        && firstPair.getSecondEmployeeId() == secondPair.getFirstEmployeeId());
    }

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

    private static PairExperience generatePairExperience(long projectId,
                                                        EmployeeProjectExperience firstEmployeeExperience,
                                                        EmployeeProjectExperience secondEmployeeExperience) {
        var overlappingMonths = ExperienceCalculator.getTotalOverlappingMonths(firstEmployeeExperience,
                secondEmployeeExperience);
        return new PairExperience(firstEmployeeExperience.getEmployeeId(), secondEmployeeExperience.getEmployeeId(),
                projectId, overlappingMonths);
    }

    private static List<EmployeeProjectExperience> getEmployeesExperienceList(long projectId, List<CsvEntry> entries) {
        var employeeGroupedEntries = entries.stream().collect(groupingBy(CsvEntry::getEmployeeId));

        return employeeGroupedEntries.entrySet().stream()
                .map(e -> new EmployeeProjectExperience(e.getKey(), projectId, getEmployeeDateRanges(e.getValue())))
                .collect(toList());
    }

    private static List<DateRange> getEmployeeDateRanges(List<CsvEntry> employeeCsvEntries) {
        List<DateRange> employeeExperienceDateRanges = new ArrayList<>();
        for (var csvEntry : employeeCsvEntries) {
            employeeExperienceDateRanges.add(new DateRange(csvEntry.getDateFrom(), csvEntry.getDateTo()));
        }

        return employeeExperienceDateRanges;
    }
}
