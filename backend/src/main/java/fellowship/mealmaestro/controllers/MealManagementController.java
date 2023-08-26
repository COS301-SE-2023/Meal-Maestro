package fellowship.mealmaestro.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fellowship.mealmaestro.models.DateModel;
import fellowship.mealmaestro.models.MealPlanModel;
import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.services.MealDatabaseService;
import fellowship.mealmaestro.services.MealManagementService;
import jakarta.validation.Valid;

@RestController
public class MealManagementController {
    @Autowired
    private MealManagementService mealManagementService;
    @Autowired
    private MealDatabaseService mealDatabaseService;

    @PostMapping("/getMealPlanForDay")
    public ResponseEntity<List<MealModel>> dailyMeals(@Valid @RequestBody DateModel request,
            @RequestHeader("Authorization") String token)
            throws JsonMappingException, JsonProcessingException {
        // admin
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        LocalDate date = request.getDate();
        // retrieve

        List<MealModel> mealsForWeek = mealDatabaseService.findUsersMealPlanForDate(date, token);
        if (mealsForWeek.size() > 0) {
            System.out.println("loaded from database");

            return ResponseEntity.ok(mealsForWeek);
        } else {
            // generate

            System.out.println("generated");

            JsonNode mealsModels = mealManagementService.generateMealPlanJson();
            // save
            List<MealModel> meals = mealDatabaseService.saveMeals(mealsModels, date, token);
            // return
            return ResponseEntity.ok(meals);
        }

    }

    @GetMapping("/getMeal")
    public String meal() throws JsonMappingException, JsonProcessingException {
        return mealManagementService.generateMeal();
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
    public String regenerate(@Valid @RequestBody MealPlanModel request, @RequestHeader("Authorization") String token)
            throws JsonMappingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        MealModel mealModel = new MealModel();
        DayOfWeek dayOfWeek = request.getMealDate();
        Optional<MealPlanModel> databaseModel = mealDatabaseService.findUsersMealPlan(dayOfWeek, token);

        if (databaseModel.isPresent()) {
            MealPlanModel newModel = databaseModel.get();
            System.out.println("present");

            String meal = request.getMeal();
            if (meal.equals("breakfast")) {

                mealModel = newModel.getBreakfast();
                mealModel = objectMapper.readValue(mealManagementService.generateMeal(request.getMeal()),
                        MealModel.class);

                newModel.setBreakfast(mealModel);
            } else if (meal.equals("lunch")) {

                mealModel = newModel.getLunch();
                mealModel = objectMapper.readValue(mealManagementService.generateMeal(request.getMeal()),
                        MealModel.class);

                newModel.setLunch(mealModel);
            } else if (meal.equals("dinner")) {

                mealModel = newModel.getDinner();
                mealModel = objectMapper.readValue(mealManagementService.generateMeal(request.getMeal()),
                        MealModel.class);

                newModel.setDinner(mealModel);
            }

            System.out.println(objectMapper.valueToTree(mealModel).toString());

            this.mealDatabaseService.saveRegeneratedMeal(newModel);

            ObjectNode MealPlanModel = objectMapper.valueToTree(newModel);

            return MealPlanModel.toString();

        }
        ObjectNode MealPlanModel = objectMapper.valueToTree(request);
        return MealPlanModel.toString();
    }

    // @GetMapping("/getPopularMeals")
    // public String popularMeals() throws JsonMappingException,
    // JsonProcessingException{
    // return mealManagementService.generatePopularMeals();
    // }

    // @GetMapping("/getSearchedMeals")
    // public String searchedMeals(@RequestParam String query) throws
    // JsonMappingException, JsonProcessingException {
    // // Call the mealManagementService to search meals based on the query
    // return mealManagementService.generateSearchedMeals(query);
    // }
}
