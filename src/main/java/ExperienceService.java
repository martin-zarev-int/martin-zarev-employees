import csv.CsvEntry;
import experience.DateRange;
import experience.EmployeeProjectExperience;
import experience.ExperienceCalculator;
import experience.PairExperience;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class ExperienceService {

    public static List<PairExperience> getPairExperienceList(List<CsvEntry> csvEntries) {
        var groupedCsvEntries = csvEntries.stream().collect(groupingBy(CsvEntry::getProjectId));

        var employeesExperienceByProject = groupedCsvEntries.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> getEmployeesExperienceList(e.getKey(), e.getValue())));

        return employeesExperienceByProject.entrySet().stream()
                .map(e -> getPairExperienceListForProject(e.getKey(), e.getValue()))
                .flatMap(Collection::stream)
                .collect(toList());
    }

    public static List<PairExperience> getPairExperienceListForProject(
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

    public static PairExperience generatePairExperience(long projectId,
                                                        EmployeeProjectExperience firstEmployeeExperience,
                                                        EmployeeProjectExperience secondEmployeeExperience) {
        var overlappingMonths = ExperienceCalculator.getTotalOverlappingMonths(firstEmployeeExperience,
                secondEmployeeExperience);
        return new PairExperience(firstEmployeeExperience.getEmployeeId(), secondEmployeeExperience.getEmployeeId(),
                projectId, overlappingMonths);
    }

    public static List<EmployeeProjectExperience> getEmployeesExperienceList(long projectId, List<CsvEntry> entries) {
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
