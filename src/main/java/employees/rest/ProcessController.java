package employees.rest;

import employees.services.CsvService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;

import static employees.services.ExperienceService.*;

@Controller
public class ProcessController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/process-csv")
    public String processCsv(@RequestParam("file") MultipartFile csvFile, Model model) {

        if (csvFile.isEmpty()) {
            return csvProcessErrorMessage("Please select a CSV file.", model);
        }

        var csvEntryList = CsvService.retrieveCsvEntries(csvFile, DateTimeFormatter.ISO_DATE);
        var pairExperienceList = getPairExperienceList(csvEntryList);
        var pairWithTheMostOverlappingOptional = getPairWithTheMostOverlappingExperience(pairExperienceList);

        if (pairWithTheMostOverlappingOptional.isEmpty()) {
            if (csvEntryList.isEmpty()) {
                return csvProcessErrorMessage("No entries were extracted from CSV.", model);
            } else if (pairExperienceList.isEmpty()) {
                return csvProcessErrorMessage("System was not able to extract pairs.", model);
            } else {
                return csvProcessErrorMessage("System was not able to find the pair with the most overlap.", model);
            }
        }

        var allExperiencesForMostOverlappingPair = extractPairsWithTheSameEmployees(pairExperienceList,
                pairWithTheMostOverlappingOptional.get());

        model.addAttribute("pairExperiences", allExperiencesForMostOverlappingPair);
        model.addAttribute("status", true);

        return "file-process-status";
    }

    private String csvProcessErrorMessage(String message, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("status", false);

        return "file-process-status";
    }
}
