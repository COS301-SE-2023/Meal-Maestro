package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fellowship.mealmaestro.models.neo4j.FoodModel;
import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.models.neo4j.PantryModel;
import fellowship.mealmaestro.models.neo4j.ViewModel;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;

public class RecommendationService {
    @Autowired
    private UserService userService;
    @Autowired
    private PantryService pantryService;
    @Autowired
    private MealDatabaseService mealDatabaseService;
    @Autowired
    private MealManagementService mealManagementService;
    
    public MealModel getRecommendedMeal(String token){
        MealModel recMealModel = new MealModel();
        //get view and pantry
        List<FoodModel> pantryModel = pantryService.getPantry(token.substring(7));
        ViewModel viewModel = userService.getUser(token).getView();
        //get positive keys
        
        // compare view and pantry

        //use list to find db meal

        //query gpt

        return recMealModel;
    }
}
