package fellowship.mealmaestro.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fellowship.mealmaestro.models.MealPlanModel;
import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.models.relationships.HasMeal;
import fellowship.mealmaestro.repositories.DaysMealsRepository;
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
            throws JsonProcessingException, IllegalArgumentException {

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
            if (user.getMeals() == null) {
                user.setMeals(new ArrayList<>());
            }
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

    public List<MealPlanModel> retrievemealPlanModel(DayOfWeek date) {
        return daysMealsRepository.findMealsForNextWeek(date);
    }

    public Optional<MealPlanModel> retrieveDatesMealModel(DayOfWeek date) {
        return daysMealsRepository.findMealsForDate(date);
    }

    public Optional<MealPlanModel> fetchDay(DayOfWeek mealDate) {
        return daysMealsRepository.findByMealDate(mealDate);
    }

    public void saveRegeneratedMeal(MealPlanModel mealPlanModel) {
        daysMealsRepository.save(mealPlanModel);
    }

    public void changeMealForDate(DayOfWeek mealDate, MealModel mealModel, String time) {

        Optional<MealPlanModel> optionalmealPlanModel = daysMealsRepository.findByMealDate(mealDate);
        if (optionalmealPlanModel.isEmpty()) {
            // Handle error, node not found for the given mealDate
            return;
        }

        MealPlanModel mealPlanModel = optionalmealPlanModel.get();

        if (time == "breakfast") {
            mealPlanModel.setBreakfast(mealModel);
            MealModel updatedMeal = mealRepository.save(mealModel);
            mealPlanModel.setBreakfast(updatedMeal);
        }
        if (time == "lunch") {
            mealPlanModel.setLunch(mealModel);
            MealModel updatedMeal = mealRepository.save(mealModel);
            mealPlanModel.setLunch(updatedMeal);
        }
        if (time == "dinner") {
            mealPlanModel.setDinner(mealModel);
            MealModel updatedMeal = mealRepository.save(mealModel);
            mealPlanModel.setDinner(updatedMeal);
        }

        daysMealsRepository.save(mealPlanModel);
    }

    public List<MealModel> findUsersMealPlanForDate(LocalDate date, String token)
            throws JsonProcessingException, IllegalArgumentException {

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

    public Optional<MealModel> findMealTypeForUser(String string, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        List<MealModel> randomMeals = mealRepository.get100RandomMeals();

        List<HasMeal> meals = user.getMeals();

        // if meal with meal type is present in randomMeals, return it
        for (MealModel meal : randomMeals) {
            if (meal.getType().equals(string)) {
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

}
