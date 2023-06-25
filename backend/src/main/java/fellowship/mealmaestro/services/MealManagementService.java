package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealManagementService {

    @Autowired private OpenaiApiService openaiApiService;

    public String generateDaysMeals(){

        return "";
    }

    public String generateMeal(){

        return "";
    }
}
