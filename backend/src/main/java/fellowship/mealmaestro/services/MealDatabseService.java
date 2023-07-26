package fellowship.mealmaestro.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fellowship.mealmaestro.models.DaysMealsModel;
import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.repositories.DaysMealsRepository;
import fellowship.mealmaestro.repositories.MealRepository;

@Service
public class MealDatabseService {
    private final MealRepository mealRepository;

    public MealRepository getMealRepository() {
        return mealRepository;
    }

    private final DaysMealsRepository daysMealsRepository;

    public DaysMealsRepository getDaysMealsRepository() {
        return daysMealsRepository;
    }

    @Autowired
    public MealDatabseService(MealRepository mealRepository, DaysMealsRepository daysMealsRepository) {
        this.daysMealsRepository = daysMealsRepository;
        this.mealRepository = mealRepository;

    }

    public void saveDaysMeals(JsonNode daysMealsJson, Date date, String token) throws JsonProcessingException, IllegalArgumentException {
        UserService userService = new UserService();
        UserModel userModel = userService.getUser(token);

        ObjectMapper objectMapper = new ObjectMapper();

        MealModel breakfast = objectMapper.treeToValue(daysMealsJson.get("breakfast"), MealModel.class);
        MealModel lunch = objectMapper.treeToValue(daysMealsJson.get("lunch"), MealModel.class);
        MealModel dinner = objectMapper.treeToValue(daysMealsJson.get("dinner"), MealModel.class);

        DaysMealsModel daysMealsModel = new DaysMealsModel(breakfast, lunch, dinner, date, userModel);
        daysMealsRepository.save(daysMealsModel);
    }

    public List<DaysMealsModel> retrieveDaysMealsModel(Date date){
        return daysMealsRepository.findMealsForNextWeek(date);
    }

     public List<DaysMealsModel> retrieveDatessMealModel(Date date){
        return daysMealsRepository.findMealsForDate(date);
    }
    
    public Optional<DaysMealsModel> fetchDay(Date mealDate) {
        return daysMealsRepository.findByMealDate(mealDate);
    }

    public void changeMealForDate(Date mealDate, MealModel mealModel, String time) {

        Optional<DaysMealsModel> optionalDaysMealsModel = daysMealsRepository.findByMealDate(mealDate);
        if (optionalDaysMealsModel.isEmpty()) {
            // Handle error, node not found for the given mealDate
            return;
        }

        DaysMealsModel daysMealsModel = optionalDaysMealsModel.get();

        if(time == "breakfast")
        {
            daysMealsModel.setBreakfast(mealModel);
            MealModel updatedMeal = mealRepository.save(mealModel);
            daysMealsModel.setBreakfast(updatedMeal);
        }
        if(time == "lunch")
        {
            daysMealsModel.setLunch(mealModel);
            MealModel updatedMeal = mealRepository.save(mealModel);
            daysMealsModel.setLunch(updatedMeal);
        }
        if(time == "dinner")
        {
            daysMealsModel.setDinner(mealModel);
            MealModel updatedMeal = mealRepository.save(mealModel);
            daysMealsModel.setDinner(updatedMeal);
        }
        
        daysMealsRepository.save(daysMealsModel);
    }
}
