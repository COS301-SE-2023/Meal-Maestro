package fellowship.mealmaestro.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import fellowship.mealmaestro.models.RegenerateMealRequest;
import fellowship.mealmaestro.models.neo4j.DateModel;
import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.services.LogService;
import fellowship.mealmaestro.services.MealDatabaseService;
import fellowship.mealmaestro.services.MealManagementService;
import jakarta.validation.Valid;

@RestController
public class MealManagementController {
    @Autowired
    private MealManagementService mealManagementService;
    @Autowired
    private MealDatabaseService mealDatabaseService;
    @Autowired
    private LogService logService;

    @PostMapping("/getMealPlanForDay")
    public ResponseEntity<List<MealModel>> dailyMeals(@Valid @RequestBody DateModel request,
            @RequestHeader("Authorization") String token) {
        // admin
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        token = token.substring(7);

        LocalDate date = request.getDate();
        // retrieve

        List<MealModel> mealsForDay = mealDatabaseService.findUsersMealPlanForDate(date, token);
        if (mealsForDay.size() == 0) {

            // look to find existing meals that are in the database
            Optional<MealModel> breakfast = mealDatabaseService.findMealTypeForUser("breakfast", token);
            Optional<MealModel> lunch = mealDatabaseService.findMealTypeForUser("lunch", token);
            Optional<MealModel> dinner = mealDatabaseService.findMealTypeForUser("dinner", token);

            // generate meals that aren't present
            if (!breakfast.isPresent()) {
                MealModel breakfastGenerated = mealManagementService.generateMeal("breakfast", token);
                mealsForDay.add(breakfastGenerated);
            } else {
                mealsForDay.add(breakfast.get());
            }
            if (!lunch.isPresent()) {
                MealModel lunchGenerated = mealManagementService.generateMeal("lunch", token);
                // if lunch name is the same as breakfast name, generate a new lunch
                // while (lunchGenerated.getName().equals(breakfast.get().getName())) {
                // lunchGenerated = mealManagementService.generateMeal("lunch", token);
                // }
                mealsForDay.add(lunchGenerated);
            } else {
                mealsForDay.add(lunch.get());
            }
            if (!dinner.isPresent()) {
                MealModel dinnerGenerated = mealManagementService.generateMeal("dinner", token);
                // if dinner name is the same as breakfast or lunch name, generate a new dinner
                // while (dinnerGenerated.getName().equals(breakfast.get().getName())
                // || dinnerGenerated.getName().equals(lunch.get().getName())) {
                // dinnerGenerated = mealManagementService.generateMeal("dinner", token);
                // }
                mealsForDay.add(dinnerGenerated);
            } else {
                mealsForDay.add(dinner.get());
            }

            // save
            List<MealModel> meals = mealDatabaseService.saveMeals(mealsForDay, date, token);
            // return
            return ResponseEntity.ok(meals);
        }

        return ResponseEntity.ok(mealsForDay);
    }

    public static JsonNode findMealSegment(JsonNode jsonNode, String mealType) {
        if (jsonNode.isObject()) {
            JsonNode startNode = jsonNode.get("start");
            if (startNode != null) {
                JsonNode startProperties = startNode.get("properties");
                if (startProperties != null) {
                    JsonNode mealDateNode = startProperties.get("mealDate");
                    if (mealDateNode != null && mealType.equalsIgnoreCase(mealDateNode.asText())) {
                        return jsonNode;
                    }
                }
            }

            JsonNode segmentsNode = jsonNode.get("segments");
            if (segmentsNode != null) {
                for (JsonNode segment : segmentsNode) {
                    JsonNode foundNode = findMealSegment(segment, mealType);
                    if (foundNode != null) {
                        return foundNode;
                    }
                }
            }
        }

        return null;
    }

    @PostMapping("/regenerate")
    public ResponseEntity<MealModel> regenerate(@RequestBody RegenerateMealRequest request,
            @RequestHeader("Authorization") String token)
            throws JsonMappingException, JsonProcessingException {

        logService.logMeal(token, request.getMeal(), "regenerate");

        token = token.substring(7);

        // Try find an appropriate meal in the database
        Optional<MealModel> replacementMeal = mealDatabaseService.findMealTypeForUser(request.getMeal().getType(),
                token);
        MealModel returnedMeal = null;

        if (replacementMeal.isPresent()) {
            returnedMeal = mealDatabaseService.replaceMeal(request, replacementMeal.get(), token);
        } else {
            // If there is no replacement, generate a new meal
            MealModel newMeal = mealManagementService.generateMeal(request.getMeal().getType(), token);
            returnedMeal = mealDatabaseService.replaceMeal(request, newMeal, token);
        }

        return ResponseEntity.ok(returnedMeal);
    }

}
