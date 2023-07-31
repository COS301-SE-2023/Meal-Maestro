package fellowship.mealmaestro.controllers;

import java.time.DayOfWeek;
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

import fellowship.mealmaestro.models.DaysMealsModel;
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

    public static class DateModel {
        private DayOfWeek dayOfWeek;

        public void setDayOfWeek(DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public DayOfWeek getDayOfWeek() {
            return this.dayOfWeek;
        }

        public DateModel() {
        };
    }

    @PostMapping("/getDaysMeals")
    public String dailyMeals(@Valid @RequestBody DateModel request, @RequestHeader("Authorization") String token)
            throws JsonMappingException, JsonProcessingException {
        // admin
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build().toString();
        }

        DayOfWeek dayOfWeek = request.getDayOfWeek();
        ObjectMapper objectMapper = new ObjectMapper();
        // retrieve
        Optional<DaysMealsModel> mealsForWeek = mealDatabseService.findUsersDaysMeals(dayOfWeek, token);
        if (mealsForWeek.isPresent()) {
            System.out.println("loaded from database");
            ObjectNode daysMealsModel = objectMapper.valueToTree(mealsForWeek.get());

            return daysMealsModel.toString();
        } else {
            // generate

            System.out.println("generated");

            JsonNode mealsModels = mealManagementService.generateDaysMealsJson();
            // +=
            // objectMapper.treeToValue(mealManagementService.generateDaysMealsJson(),DaysMealsModel.class);
            // save
            mealDatabseService.saveDaysMeals(mealsModels, dayOfWeek, token);
            // return
            return mealsModels.toString();
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
    public String regenerate(@Valid @RequestBody DaysMealsModel request, @RequestHeader("Authorization") String token)
            throws JsonMappingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        MealModel mealModel = new MealModel();
        DayOfWeek dayOfWeek = request.getMealDate();
        Optional<DaysMealsModel> databaseModel = mealDatabseService.findUsersDaysMeals(dayOfWeek, token);

        if (databaseModel.isPresent()) {
            DaysMealsModel newModel = databaseModel.get();
            System.out.println("present");
            System.out.println(request.getMeal());
            String meal = request.getMeal();
            if (meal.equals("breakfast")) {
                System.out.println("b");
                mealModel = newModel.getBreakfast();
                mealModel = objectMapper.readValue(mealManagementService.generateMeal(request.getMeal()),
                        MealModel.class);
                mealModel.setName("Cookies and creme");
                newModel.setBreakfast(mealModel);
            } else if (meal.equals("lunch")) {
                System.out.println("l");
                mealModel = newModel.getLunch();
                mealModel = objectMapper.readValue(mealManagementService.generateMeal(request.getMeal()),
                        MealModel.class);
                mealModel.setName("Cookies and creme");
                newModel.setLunch(mealModel);
            } else if (meal.equals("dinner")) {
                System.out.println("d");
                mealModel = newModel.getDinner();
                mealModel = objectMapper.readValue(mealManagementService.generateMeal(request.getMeal()),
                        MealModel.class);
                mealModel.setName("Cookies and creme");
                newModel.setDinner(mealModel);
            }

            System.out.println(objectMapper.valueToTree(mealModel).toString());

            this.mealDatabseService.saveRegeneratedMeal(newModel);

            ObjectNode daysMealsModel = objectMapper.valueToTree(newModel);
            // regenerate and update

            return daysMealsModel.toString();

            // return databaseModel.get().toString();
        }
        ObjectNode daysMealsModel = objectMapper.valueToTree(request);
        return daysMealsModel.toString();
    }

}
