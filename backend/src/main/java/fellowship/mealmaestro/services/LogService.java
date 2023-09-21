package fellowship.mealmaestro.services;

import java.io.Console;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.models.neo4j.relationships.HasLogEntry;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;

@Service
public class LogService {
    @Autowired
    private UserService userService;
    @Autowired
    private MealDatabaseService mealDatabaseService;
    @Autowired 
    private UserRepository userRepository;

    public void logMeal(String token, MealModel meal, String entryType){
        UserModel user = userService.getUser(token);
        Optional<MealModel> dbMeal = mealDatabaseService.findMealForUser(meal.getName(), token);
        if(dbMeal.isPresent())
            {
                HasLogEntry entry = new HasLogEntry(dbMeal.get(),LocalDate.now(), entryType);
                user.getEntries().add(entry);
                userRepository.save(user);
                System.out.println("LogEntry saved! ("+ meal.getName() +","+ entryType +")");
            }
    }
}
