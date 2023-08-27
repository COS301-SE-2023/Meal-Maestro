package fellowship.mealmaestro.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.models.relationships.HasMeal;
import fellowship.mealmaestro.repositories.MealRepository;
import fellowship.mealmaestro.repositories.UserRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class MealDatabaseService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

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
        HasMeal breakfastHasMeal = new HasMeal(breakfast, date);
        HasMeal lunchHasMeal = new HasMeal(lunch, date);
        HasMeal dinnerHasMeal = new HasMeal(dinner, date);

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

    public List<MealModel> findUsersMealPlanForDate(LocalDate date, String token) {

        removeOldMeals(date, token);

        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();

        List<HasMeal> meals = user.getMeals();

        List<MealModel> mealModels = new ArrayList<MealModel>();

        for (HasMeal meal : meals) {
            if (meal.getDate().equals(date)) {
                mealModels.add(meal.getMeal());
            }
        }

        return mealModels;
    }

    public void removeOldMeals(LocalDate date, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();

        List<HasMeal> meals = user.getMeals();

        List<HasMeal> mealsToRemove = new ArrayList<HasMeal>();

        // if meal date is before date, remove it
        for (HasMeal meal : meals) {
            if (meal.getDate().isBefore(date)) {
                mealsToRemove.add(meal);
            }
        }

        meals.removeAll(mealsToRemove);

        userRepository.save(user);
    }

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
        String[] ingredientsArray = ingredients.split("\\s+");
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

    public MealModel replaceMeal(MealModel oldMeal, MealModel newMeal, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();

        List<HasMeal> meals = user.getMeals();

        for (HasMeal meal : meals) {
            if (meal.getMeal().getName().equals(oldMeal.getName())) {
                meal.setMeal(newMeal);
            }
        }

        userRepository.save(user);

        return newMeal;
    }

}
