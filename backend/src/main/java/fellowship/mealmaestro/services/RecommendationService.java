package fellowship.mealmaestro.services;

import java.util.ArrayList;
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

    private final Double MIN_VALUE = -0.01;

    public MealModel getRecommendedMeal(String token) throws Exception {
        MealModel recMealModel = new MealModel();
        // get view and pantry
        List<String> pantryItems = userService.getUser(token).getPantry().getNameList();
        List<String> validIngredients = userService.getUser(token).getView().getPositiveNScores(MIN_VALUE);

        // compare view and pantry

        // use list to find db meal

        // query gpt

        return recMealModel;
    }

    public static List<String> findCommonItems(List<String> list1, List<String> list2) {
        List<String> commonItems = new ArrayList<>();

        for (String item1 : list1) {
            for (String item2 : list2) {
                if (item2.contains(item1)) {
                    commonItems.add(item1);
                    break;
                }
                if (item1.contains(item2)) {
                    commonItems.add(item2);
                    break;
                }
            }
        }

        return commonItems;
    }
}
