package fellowship.mealmaestro.services;

import java.util.ArrayList;
import java.util.List;

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

     public String generatePopularMeals()throws JsonMappingException, JsonProcessingException {
        
        JsonNode mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast lunch or dinner"));
        int i = 0;
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
        
        // JsonNode mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast lunch or dinner"));
        // ObjectNode combinedNode = JsonNodeFactory.instance.objectNode();
        // for (int i=0;i<3;i++) {
        //     // String entities = mealJson.get(i).toString();
        //      combinedNode.set("breakfast lunch or dinner", mealJson.get(i));
        // }
        //        //
        // // DaysMeals daysMeals = objectMapper.treeToValue(combinedNode, DaysMeals.class);
        // return combinedNode.toString();

        // Fetch popular meals in JSON form
    //     int i = 0;
    //     JsonNode mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast lunch or dinner"));
    //     List<JsonNode> mealEntities = new ArrayList<>();

    //     // if (mealJson.isArray()) {
    //         for (JsonNode entity : mealJson) {
    //             mealEntities.add(entity, toString());
    //         }
    //   //  }

    //     while (i < 3) {
    //        // JsonNode mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast lunch or dinner"));
    //         if (!mealJson.isMissingNode()) {
    //             mealEntities.add(mealJson);
    //             i++;
    //         }
    //     }

    //     return mealEntities.toString();


    //     int i = 0;
    // JsonNode mealJson = null;

    //     int maxAttempts = 3;
    //     JsonNode breakfastJson = fetchMealJson("breakfast", maxAttempts);



       // return fetchMealResponse("breakfast lunch or dinner");

        // int i = 0;
        // JsonNode popularMealJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast lunch or dinner"));
        // if(popularMealJson.isMissingNode())
        // {
        //     int prevBestOfN = openaiApiService.getBestofN();
        //     Boolean success = false;
        //     openaiApiService.setBestofN(prevBestOfN + 1);
        //     while(!success&& i < 5)
        //     {
        //         popularMealJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
        //         if(!popularMealJson.isMissingNode())
        //             success = true;
        //             i++;
        //     }
        //     openaiApiService.setBestofN(prevBestOfN);
        // }
        //  return popularMealJson.toString();

    }

    public String generateSearchedMeals(String query) throws JsonProcessingException {
        int i = 0;
        JsonNode searchedMeal = objectMapper.readTree(openaiApiService.fetchMealResponse(query));
        if (searchedMeal.isMissingNode()) {
            int prevBestOfN = openaiApiService.getBestofN();
            boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while (!success && i < 5) {
                searchedMeal = objectMapper.readTree(openaiApiService.fetchMealResponse(query));
                if (!searchedMeal.isMissingNode())
                    success = true;
                i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        return searchedMeal.toString();
    }

    // public List<Meal> generateSearchedMeals(String query) throws JsonProcessingException {
    //     int i = 0;
    //     List<Meal> searchedMeals = new ArrayList<>();

    //     JsonNode mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse(query));
    //     if (!mealJson.isMissingNode()) {
    //         searchedMeals.add(parseMealFromJson(mealJson));
    //     } else {
    //         int prevBestOfN = openaiApiService.getBestofN();
    //         boolean success = false;
    //         openaiApiService.setBestofN(prevBestOfN + 1);
    //         while (!success && i < 5) {
    //             mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse(query));
    //             if (!mealJson.isMissingNode()) {
    //                 searchedMeals.add(parseMealFromJson(mealJson));
    //                 success = true;
    //             }
    //             i++;
    //         }
    //         openaiApiService.setBestofN(prevBestOfN);
    //     }

    //     return searchedMeals;
    // }

    // private Meal parseMealFromJson(JsonNode mealJson) {
    //     // Implement the logic to parse the JSON response and construct a Meal object
    //     // based on the data received from the OpenAI API.
    //     // You'll need to extract the necessary information from mealJson and create the Meal object.
    //     // Replace Meal with the appropriate class representing a meal in your application.
    //     return new Meal(/* meal properties from mealJson */);
    // }
}
