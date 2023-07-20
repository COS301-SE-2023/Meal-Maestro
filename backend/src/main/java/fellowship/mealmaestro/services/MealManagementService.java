package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class MealManagementService {

    @Autowired
    private OpenaiApiService openaiApiService;
    @Autowired
    private ObjectMapper objectMapper;

    public String generateDaysMeals() throws JsonMappingException, JsonProcessingException {
        int i = 0;
        JsonNode breakfastJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
        if(breakfastJson.isMissingNode())
        {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while(!success && i < 5)
            {
                breakfastJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
                if(!breakfastJson.isMissingNode())
                    success = true;
                    i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        JsonNode lunchJson = objectMapper.readTree(openaiApiService.fetchMealResponse("lunch"));
        if(lunchJson.isMissingNode())
        {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while(!success&& i < 5)
            {
                lunchJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
                if(!lunchJson.isMissingNode())
                    success = true;
                    i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        JsonNode dinnerJson = objectMapper.readTree(openaiApiService.fetchMealResponse("dinner"));
        if(dinnerJson.isMissingNode())
        {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while(!success&& i < 5)
            {
                dinnerJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
                if(!dinnerJson.isMissingNode())
                    success = true;
                    i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        ObjectNode combinedNode = JsonNodeFactory.instance.objectNode();
        combinedNode.set("breakfast", breakfastJson);
        combinedNode.set("lunch", lunchJson);
        combinedNode.set("dinner", dinnerJson);
        //
        // DaysMeals daysMeals = objectMapper.treeToValue(combinedNode, DaysMeals.class);
        return combinedNode.toString();
    }

    public String generateMeal() throws JsonMappingException, JsonProcessingException {
        int i = 0;
        JsonNode mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast lunch or dinner"));
        if(mealJson.isMissingNode())
        {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while(!success&& i < 5)
            {
                mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
                if(!mealJson.isMissingNode())
                    success = true;
                    i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        return mealJson.toString();
    }

     public String generatePopularMeals() {
        // Fetch popular meals in JSON form


         

        // return popularMealsJSON.toString();
    }
}
