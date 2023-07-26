package fellowship.mealmaestro.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import fellowship.mealmaestro.models.DaysMealsModel;
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

    public void saveDaysMeals(JsonNode daysMealsJson, Date date) {
        DaysMealsModel daysMealsModel;
        

    }
}
