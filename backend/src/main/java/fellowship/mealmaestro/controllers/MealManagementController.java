package fellowship.mealmaestro.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fellowship.mealmaestro.models.DaysMealsModel;
import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.services.MealDatabseService;
import fellowship.mealmaestro.services.MealManagementService;
import jakarta.validation.Valid;

@RestController
public class MealManagementController {
    @Autowired
    private MealManagementService mealManagementService;
    @Autowired
    private MealDatabseService mealDatabseService;

    @GetMapping("/getDaysMeals")
    public String dailyMeals(@Valid @RequestBody @RequestHeader("Authorization") String token) throws JsonMappingException, JsonProcessingException{
        //admin
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build().toString();
        }
        String authToken = token.substring(7);
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // retrieve
        List<DaysMealsModel> mealsForWeek = mealDatabseService.retrieveDatessMealModel(currentDate);
        if(!mealsForWeek.isEmpty())
        {
            return mealsForWeek.toString();
        }
        else 
        {
        //generate
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode mealsModels = mealManagementService.generateDaysMealsJson();
            //+= objectMapper.treeToValue(mealManagementService.generateDaysMealsJson(),DaysMealsModel.class);
        //save
            mealDatabseService.saveDaysMeals(mealsModels, currentDate, authToken);
        //return
        return mealsForWeek.toString();
        }
      
    }

    @GetMapping("/getMeal")
    public String meal() throws JsonMappingException, JsonProcessingException{
        return mealManagementService.generateMeal();
    }

}
