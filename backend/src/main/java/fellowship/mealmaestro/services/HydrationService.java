package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.models.neo4j.ViewModel;
import fellowship.mealmaestro.models.neo4j.relationships.HasLogEntry;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class HydrationService {
    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 20 * 60 * 1000)
    public void pollLogs() {
        List<UserModel> userList = userRepository.findUsersWithNewLogEntries();
        // per user
        for (UserModel user : userList) {
            List<HasLogEntry> logEntries = userRepository.findUnprocessedLogEntriesForUser(user);
            ViewModel viewModel = user.getView();

            for (HasLogEntry entry : logEntries) {

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
            }

            user.setView(viewModel);
            userRepository.save(user);
        }

    }

    // helper functions to be done
    // trim
    private static String trimCharacters(String input) {
        String regex = "[0-9\\s]+";
        String result = input.replaceAll(regex, "");
        return result;
    }

    // convert
    private static List<String> parseCommaSeparatedString(String input) {
        String[] elements = input.split(",");
        List<String> result = Arrays.asList(elements);

        return result;
    }

    // scores
    private static Double getScoreValue(String entryType) {
        switch (entryType.toLowerCase()) {
            case "regenerated":
                return -0.4;
                
            case "like":
                return 1.0;

            case "dislike":
                return -0.7;

            case "save":
                return 0.7;

            default:
                return 0.5;

        }
    }
}
