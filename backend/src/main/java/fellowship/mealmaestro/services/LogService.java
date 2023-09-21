package fellowship.mealmaestro.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.models.neo4j.relationships.HasLogEntry;
import fellowship.mealmaestro.models.neo4j.relationships.HasMeal;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;

@Service
public class LogService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    public void logMeal(String token, MealModel meal, String entryType) {
        UserModel user = userService.getUser(token);
        MealModel dbMeal = null;
        for (HasMeal m : user.getMeals()) {
            if (m.getMeal().getName().equals(meal.getName())) {
                dbMeal = m.getMeal();
            }
        }
        if (dbMeal == null) {
            System.out.println("logging failed");
            return;
        }
        HasLogEntry entry = new HasLogEntry(dbMeal, LocalDate.now(), entryType);
        user.getEntries().add(entry);
        userRepository.save(user);
        System.out.println("LogEntry saved! (" + meal.getName() + "," + entryType + ")");

    }

    public List<HasLogEntry> findUnprocessedLogEntriesForUser(UserModel user) {
        return userRepository.findUnprocessedLogEntriesForUser(user);
    }
}
