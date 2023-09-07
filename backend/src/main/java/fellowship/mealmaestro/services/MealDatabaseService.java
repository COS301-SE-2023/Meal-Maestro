package fellowship.mealmaestro.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fellowship.mealmaestro.models.RegenerateMealRequest;
import fellowship.mealmaestro.models.neo4j.FoodModel;
import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.models.neo4j.relationships.HasMeal;
import fellowship.mealmaestro.repositories.neo4j.MealRepository;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class MealDatabaseService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<MealModel> saveMeals(List<MealModel> mealsToSave, LocalDate date, String token)
            throws IllegalArgumentException {

        // Step 1: Create the MealModel
        MealModel breakfast = mealsToSave.get(0);
        MealModel lunch = mealsToSave.get(1);
        MealModel dinner = mealsToSave.get(2);

        // Step 2: Save the MealModel to the database
        breakfast = mealRepository.save(breakfast);
        lunch = mealRepository.save(lunch);
        dinner = mealRepository.save(dinner);

        // Step 3: Create a HasMeal relationship between the MealModel and User
        HasMeal breakfastHasMeal = new HasMeal(breakfast, date, "breakfast");
        HasMeal lunchHasMeal = new HasMeal(lunch, date, "lunch");
        HasMeal dinnerHasMeal = new HasMeal(dinner, date, "dinner");

        // Step 4: Add the HasMeal relationship to the User
        String email = jwtService.extractUserEmail(token);
        Optional<UserModel> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            UserModel user = optionalUser.get();
            user.getMeals().add(breakfastHasMeal);
            user.getMeals().add(lunchHasMeal);
            user.getMeals().add(dinnerHasMeal);

            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }

        // Step 5: Return list of breakfast, lunch, dinner
        List<MealModel> meals = new ArrayList<MealModel>();
        meals.add(breakfast);
        meals.add(lunch);
        meals.add(dinner);
        return meals;
    }

    @Transactional
    public List<MealModel> findUsersMealPlanForDate(LocalDate date, String token) {

        removeOldMeals(token);

        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();

        List<HasMeal> meals = user.getMeals();

        List<MealModel> mealModels = new ArrayList<MealModel>();

        for (HasMeal meal : meals) {
            if (meal.getDate().equals(date)) {
                mealModels.add(meal.getMeal());
            }
        }

        Collections.sort(mealModels, MEAL_TYPE_COMPARATOR);

        return mealModels;
    }

    public void removeOldMeals(String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();

        List<HasMeal> meals = user.getMeals();

        List<HasMeal> mealsToRemove = new ArrayList<HasMeal>();

        // if meal date is before today, remove it
        LocalDate today = LocalDate.now();
        for (HasMeal meal : meals) {
            if (meal.getDate().isBefore(today)) {
                mealsToRemove.add(meal);
            }
        }

        meals.removeAll(mealsToRemove);

        userRepository.save(user);
    }

    @Transactional
    public Optional<MealModel> findMealTypeForUser(String type, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        List<MealModel> randomMeals = mealRepository.get100RandomMeals();

        // if meal with meal type is present in randomMeals, return it
        for (MealModel meal : randomMeals) {
            if (meal.getType().equals(type)) {
                if (canMakeMeal(user.getPantry().getFoods(), meal.getIngredients())) {
                    return Optional.of(meal);
                }
            }
        }

        return Optional.empty();
    }

    public boolean canMakeMeal(List<FoodModel> pantryItems, String ingredients) {
        String[] ingredientsArray = ingredients.split(",");
        for (String ingredient : ingredientsArray) {
            boolean found = false;
            for (FoodModel pantryItem : pantryItems) {
                if (pantryItem.getName().toLowerCase().contains(ingredient.toLowerCase())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public MealModel replaceMeal(RegenerateMealRequest request, MealModel newMeal, String token) {
        String email = jwtService.extractUserEmail(token);

        MealModel oldMeal = request.getMeal();
        LocalDate date = request.getDate();
        mealRepository.save(newMeal);
        mealRepository.replaceMeal(date, oldMeal.getName(), newMeal.getName(), email, oldMeal.getType());
        return newMeal;
    }

    private static final Comparator<MealModel> MEAL_TYPE_COMPARATOR = new Comparator<MealModel>() {
        @Override
        public int compare(MealModel m1, MealModel m2) {
            return orderMealType(m1.getType()).compareTo(orderMealType(m2.getType()));
        }

        private Integer orderMealType(String type) {
            switch (type) {
                case "breakfast":
                    return 1;
                case "lunch":
                    return 2;
                case "dinner":
                    return 3;
                default:
                    return 4;
            }
        }
    };

}
