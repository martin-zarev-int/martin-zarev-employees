import employees.csv.CsvEntry;
import employees.experience.DateRange;
import employees.experience.EmployeeProjectExperience;
import employees.experience.ExperienceCalculator;
import employees.experience.PairExperience;
import employees.services.CsvService;
import employees.services.ExperienceService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class Testing {


    public static void main(String... args) throws URISyntaxException, IOException {

        var path = Paths.get("C:\\Users\\Stalker\\Documents\\sample.employees.csv");
        //var path = Paths.get(ClassLoader.getSystemResource("C:\\Users\\Stalker\\Documents\\sample.employees.csv").toURI());
        var entries = CsvService.retrieveCsvEntries(path, DateTimeFormatter.ISO_DATE);
        var pairExperienceList = ExperienceService.getPairExperienceList(entries);
        var maxExperiencePairOptional = pairExperienceList.stream()
                .max(Comparator.comparing(PairExperience::getOverlappingMonths));

        if (maxExperiencePairOptional.isPresent()) {
            System.out.println("max is " + maxExperiencePairOptional.get());
        }

//        var groupedCsvEntries = entries.stream().collect(groupingBy(CsvEntry::getProjectId));
//
//        for(var csvMapEntries : groupedCsvEntries.entrySet()) {
//            getEmployeeExperiences(csvMapEntries.getValue(), csvMapEntries.getKey());
//        }
//
//        int i = 0;
    }

    public static void getEmployeeExperiences(List<CsvEntry> csvEntries, long projectId) {
        var employeeGroupedEntries = csvEntries.stream().collect(groupingBy(CsvEntry::getEmployeeId));

        List<EmployeeProjectExperience> employeeProjectExperiences = new ArrayList<>();

        for (var employeeCsvEntries : employeeGroupedEntries.entrySet()) {

            List<DateRange> employeeExperienceDateRanges = new ArrayList<>();
            for (var csvEntry : employeeCsvEntries.getValue()) {
                employeeExperienceDateRanges.add(new DateRange(csvEntry.getDateFrom(), csvEntry.getDateTo()));
            }

            employeeProjectExperiences.add(new EmployeeProjectExperience(employeeCsvEntries.getKey(), projectId,
                    employeeExperienceDateRanges));
        }

        List<PairExperience> pairExperienceList = new ArrayList<>();
        for (int i = 0; i < employeeProjectExperiences.size() - 1; i++) {
            for(int j = i + 1; j < employeeProjectExperiences.size(); j++) {
                pairExperienceList.add(generatePairExperience(employeeProjectExperiences.get(i),
                        employeeProjectExperiences.get(j), projectId));
            }
        }


        int i = 0;

    }

    public static PairExperience generatePairExperience(EmployeeProjectExperience firstEmployeeExperience,
                                                        EmployeeProjectExperience secondEmployeeExperience,
                                                        long projectId) {
        var overlappingMonths = ExperienceCalculator.getTotalOverlappingMonths(firstEmployeeExperience,
                secondEmployeeExperience);
        return new PairExperience(firstEmployeeExperience.getEmployeeId(), secondEmployeeExperience.getEmployeeId(),
                projectId, overlappingMonths);
    }
}
