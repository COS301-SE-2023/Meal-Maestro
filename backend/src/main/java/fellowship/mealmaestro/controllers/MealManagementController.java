package fellowship.mealmaestro.controllers;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fellowship.mealmaestro.models.DaysMealsModel;
import fellowship.mealmaestro.services.MealDatabseService;
import fellowship.mealmaestro.services.MealManagementService;

@RestController
public class MealManagementController {
    @Autowired
    private MealManagementService mealManagementService;
    @Autowired
    private MealDatabseService mealDatabseService;

    @GetMapping("/getDaysMeals")
    public String dailyMeals( @RequestHeader("Authorization") String token) throws JsonMappingException, JsonProcessingException{
        //admin
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build().toString();
        }
        SimpleDateFormat df = new SimpleDateFormat("E");
   
        Date currentDate = new Date();
        LocalDate localDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
       ObjectMapper objectMapper = new ObjectMapper();
        // retrieve
        Optional<DaysMealsModel> mealsForWeek = mealDatabseService.findUsersDaysMeals(dayOfWeek, token);
        if(mealsForWeek.isPresent())
        {
            ObjectNode daysMealsModel = objectMapper.valueToTree(mealsForWeek.get());
            
            // JsonNode breakfastJson = findMealSegment(daysMealsModel, "breakfast");
            // JsonNode lunchJson = findMealSegment(daysMealsModel, "lunch");
            // JsonNode dinnerJson = findMealSegment(daysMealsModel, "dinner");

            // ObjectNode combinedNode = JsonNodeFactory.instance.objectNode();
            // combinedNode.set("breakfast", breakfastJson);
            // combinedNode.set("lunch", lunchJson);
            // combinedNode.set("dinner", dinnerJson);
           
           
           
            return daysMealsModel.toString();
        }
        else 
        {
        //generate
           
            JsonNode mealsModels = mealManagementService.generateDaysMealsJson();
            //+= objectMapper.treeToValue(mealManagementService.generateDaysMealsJson(),DaysMealsModel.class);
        //save
            mealDatabseService.saveDaysMeals(mealsModels, dayOfWeek, token);
        //return
        return mealsModels.toString();
        }
      
    }

    @GetMapping("/getMeal")
    public String meal() throws JsonMappingException, JsonProcessingException{
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

   
}
