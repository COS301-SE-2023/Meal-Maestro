package fellowship.mealmaestro.controllers;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;

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
        Optional<DaysMealsModel> mealsForWeek = mealDatabseService.retrieveDatesMealModel(dayOfWeek);
        if(mealsForWeek.isPresent())
        {
            JsonNode daysMealsModel = objectMapper.valueToTree(mealsForWeek.get());
            return jsonNodeToString(daysMealsModel);
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

    public static String jsonNodeToString(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            return objectToString(jsonNode);
        } else if (jsonNode.isArray()) {
            return arrayToString(jsonNode);
        } else {
            // For primitive values or leaf nodes, append the value directly
            return jsonNode.toString();
        }
    }

    private static String objectToString(JsonNode jsonNode) {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        boolean isFirst = true;

        // Get an iterator for the field names of the JSON object
        Iterator<Entry<String, JsonNode>> fields = jsonNode.fields();

        // Traverse through each property in the object
        while (fields.hasNext()) {
            Entry<String, JsonNode> field = fields.next();
            String propertyName = field.getKey();
            JsonNode propertyValue = field.getValue();

            if (!isFirst) {
                sb.append(",");
            }

            // Append the property name and recursively call the method for nested JSON nodes
            sb.append("\"").append(propertyName).append("\":");
            sb.append(jsonNodeToString(propertyValue));

            isFirst = false;
        }

        sb.append("}");

        return sb.toString();
    }

    private static String arrayToString(JsonNode jsonNode) {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        boolean isFirst = true;

        // Traverse through each element in the array
        for (JsonNode element : jsonNode) {
            if (!isFirst) {
                sb.append(",");
            }

            // Recursively call the method for elements in the array
            sb.append(jsonNodeToString(element));

            isFirst = false;
        }

        sb.append("]");

        return sb.toString();
    }
}
