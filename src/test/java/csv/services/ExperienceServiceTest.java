package csv.services;

import employees.csv.CsvEntry;
import employees.experience.PairExperience;
import employees.services.ExperienceService;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static employees.services.ExperienceService.extractPairsWithTheSameEmployees;
import static employees.services.ExperienceService.getPairWithTheMostOverlappingExperience;
import static java.time.temporal.ChronoUnit.MONTHS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExperienceServiceTest {

    @Test
    public void testGetPairWithTheMostOverlappingExperience() {
        var firstExperience = new PairExperience(3, 5, 20, 8);
        var secondExperience =  new PairExperience(1, 2, 10, 5);
        var thirdExperience = new PairExperience(6, 4, 33, 1);
        var pairExperienceList = List.of(firstExperience, secondExperience, thirdExperience);

        var mostOverlappingPairOptional = getPairWithTheMostOverlappingExperience(pairExperienceList);
        assertTrue(mostOverlappingPairOptional.isPresent());
        assertEquals(firstExperience, mostOverlappingPairOptional.get());
    }

    @Test
    public void testExtractPairsWithTheSameEmployees() {
        var pairExperienceList = List.of(
                new PairExperience(1, 3, 100, 3),
                new PairExperience(3, 1, 200, 1),
                new PairExperience(1, 3, 33, 5),
                new PairExperience(4, 5, 99, 1)
        );

        var extractedPairs = extractPairsWithTheSameEmployees(pairExperienceList,
                new PairExperience(3, 1, 0, 0));

        assertEquals(3, extractedPairs.size());
    }

    @Test
    public void testGetPairExperienceList() {
        var now = LocalDate.now();
        var csvEntries = List.of(
                new CsvEntry(1, 33, now.minus(10, MONTHS), now.minus(2, MONTHS)),
                new CsvEntry(1, 33, now, now.plus(8, MONTHS)),
                new CsvEntry(2, 33, now.minus(25, MONTHS), now.minus(13, MONTHS)),
                new CsvEntry(2, 33, now.minus(3, MONTHS), now.plus(9, MONTHS)),
                new CsvEntry(3, 666, now.minus(6, MONTHS), now.plus(6, MONTHS))
        );

        var pairExperienceList = ExperienceService.getPairExperienceList(csvEntries);
        assertEquals(1, pairExperienceList.size());
        assertEquals(9, pairExperienceList.get(0).getOverlappingMonths());
    }
}
