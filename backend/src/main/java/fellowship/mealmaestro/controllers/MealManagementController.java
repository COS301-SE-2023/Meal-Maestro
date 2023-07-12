package fellowship.mealmaestro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fellowship.mealmaestro.services.MealManagementService;

@RestController
public class MealManagementController {
    @Autowired
    private MealManagementService mealManagementService;

    @GetMapping("/getDaysMeals")
    public String dailyMeals() throws JsonMappingException, JsonProcessingException{
        return mealManagementService.generateDaysMeals();
    }

    @GetMapping("/getMeal")
    public String meal() throws JsonMappingException, JsonProcessingException{
        return mealManagementService.generateMeal();
    }

}
