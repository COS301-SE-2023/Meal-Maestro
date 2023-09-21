package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.models.neo4j.UserModel;

@Service
public class LogService {
    @Autowired
    private UserService userService;

    public void logMeal(String token, MealModel meal, String entryType){
        UserModel user = userService.getUser(token);
        
    }
}
