package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.models.neo4j.ViewModel;
import fellowship.mealmaestro.models.neo4j.relationships.HasLogEntry;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class HydrationService {
    @Autowired
    private UserRepository userRepository;
    @Transactional
    @Scheduled(fixedRate =  1 * 60 * 1000)
    public void pollLogs() {
        List<UserModel> userList = userRepository.findUsersWithNewLogEntries();
        // per user
        for (UserModel nuser : userList) {
            UserModel user = userRepository.findByEmail(nuser.getEmail()).get();
            ViewModel viewModel = user.getView();
            if(viewModel == null)
            {
                viewModel = new ViewModel();
            }
            for (int i = 0; i < user.getEntries().size();i++) {
                 HasLogEntry entry = user.getEntries().remove(i);
                String ingredientString = entry.getMeal().getIngredients();
                // trim ingredient list
                ingredientString = trimCharacters(ingredientString);
                // convert to List<String>
                List<String> ingredientList = parseCommaSeparatedString(ingredientString);
                // Scores ++ * multiplier
                Double S_MULTIPLIER = getScoreValue(entry.getEntryType());

                // update view model
                for (String ingredient : ingredientList) {
                    viewModel.updateScore(ingredient, S_MULTIPLIER);
                }

                // set processed
                entry.setProcessed(true);
                user.getEntries().add(entry);
            }
            user.setView(viewModel);
            userRepository.save(user);
        }

    }

    // helper functions to be done
    // trim
    private static String trimCharacters(String input) {
        String regex = "\\d+(?:/\\d+)?(?:\\s*(?:tablespoon|tablespoons|teaspoon|teaspoons|cup|cups|ounce|ounces|gram|grams|milliliter|milliliters|liter|liters|pound|pounds|kg|kilo|kilogram|kilograms|gallon|gallons))?";
        String result = input.replaceAll(regex, "");
        return result.toLowerCase();
    }

    // convert
    private static List<String> parseCommaSeparatedString(String input) {
        return Arrays.asList(input.split(","));
    }

    // scores
    private static Double getScoreValue(String entryType) {
        switch (entryType.toLowerCase()) {
            case "regenerated":
                return -0.2;
                
            case "like":
                return 1.0;

            case "dislike":
                return -0.5;

            case "save":
                return 0.7;

            default:
                return 0.5;

        }
    }
}
