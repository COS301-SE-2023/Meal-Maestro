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
        if (breakfastJson.isMissingNode()) {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while (!success && i < 5) {
                breakfastJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
                if (!breakfastJson.isMissingNode())
                    success = true;
                i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        JsonNode lunchJson = objectMapper.readTree(openaiApiService.fetchMealResponse("lunch"));
        if (lunchJson.isMissingNode()) {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while (!success && i < 5) {
                lunchJson = objectMapper.readTree(openaiApiService.fetchMealResponse("lunch"));
                if (!lunchJson.isMissingNode())
                    success = true;
                i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        JsonNode dinnerJson = objectMapper.readTree(openaiApiService.fetchMealResponse("dinner"));
        if (dinnerJson.isMissingNode()) {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while (!success && i < 5) {
                dinnerJson = objectMapper.readTree(openaiApiService.fetchMealResponse("dinner"));
                if (!dinnerJson.isMissingNode())
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
        // DaysMeals daysMeals = objectMapper.treeToValue(combinedNode,
        // DaysMeals.class);
        return combinedNode.toString();
    }

    public JsonNode generateMealPlanJson() throws JsonMappingException, JsonProcessingException {
        int i = 0;
        JsonNode breakfastJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
        if (breakfastJson.isMissingNode()) {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while (!success && i < 5) {
                breakfastJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
                if (!breakfastJson.isMissingNode())
                    success = true;
                i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        JsonNode lunchJson = objectMapper.readTree(openaiApiService.fetchMealResponse("lunch"));
        if (lunchJson.isMissingNode()) {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while (!success && i < 5) {
                lunchJson = objectMapper.readTree(openaiApiService.fetchMealResponse("lunch"));
                if (!lunchJson.isMissingNode())
                    success = true;
                i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        JsonNode dinnerJson = objectMapper.readTree(openaiApiService.fetchMealResponse("dinner"));
        if (dinnerJson.isMissingNode()) {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while (!success && i < 5) {
                dinnerJson = objectMapper.readTree(openaiApiService.fetchMealResponse("dinner"));
                if (!dinnerJson.isMissingNode())
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
        // DaysMeals daysMeals = objectMapper.treeToValue(combinedNode,
        // DaysMeals.class);
        return combinedNode;
    }

    public String generateMeal() throws JsonMappingException, JsonProcessingException {
        int i = 0;
        JsonNode mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast lunch or dinner"));
        if (mealJson.isMissingNode()) {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while (!success && i < 5) {
                mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast lunch or dinner"));
                if (!mealJson.isMissingNode())
                    success = true;
                i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        return mealJson.toString();
    }

    public String generateMeal(String mealType) throws JsonMappingException, JsonProcessingException {

        JsonNode mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse(mealType));
        int i = 0;
        if (mealJson.isMissingNode()) {
            int prevBestOfN = openaiApiService.getBestofN();
            Boolean success = false;
            openaiApiService.setBestofN(prevBestOfN + 1);
            while (!success && i < 4) {
                mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse(mealType));
                if (!mealJson.isMissingNode())
                    success = true;
                i++;
            }
            openaiApiService.setBestofN(prevBestOfN);
        }
        return mealJson.toString();
    }

    // public String generatePopularMeals()throws JsonMappingException,
    // JsonProcessingException {

    // JsonNode mealJson =
    // objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast lunch or
    // dinner"));
    // int i = 0;
    // if(mealJson.isMissingNode())
    // {
    // int prevBestOfN = openaiApiService.getBestofN();
    // Boolean success = false;
    // openaiApiService.setBestofN(prevBestOfN + 1);
    // while(!success&& i < 5)
    // {
    // mealJson =
    // objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
    // if(!mealJson.isMissingNode())
    // success = true;
    // i++;
    // }
    // openaiApiService.setBestofN(prevBestOfN);
    // }
    // return mealJson.toString();

    // }

    // public String generateSearchedMeals(String query) throws
    // JsonProcessingException {

    // JsonNode mealJson =
    // objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast lunch or
    // dinner"));
    // int i = 0;
    // if(mealJson.isMissingNode())
    // {
    // int prevBestOfN = openaiApiService.getBestofN();
    // Boolean success = false;
    // openaiApiService.setBestofN(prevBestOfN + 1);
    // while(!success&& i < 5)
    // {
    // mealJson =
    // objectMapper.readTree(openaiApiService.fetchMealResponse("breakfast"));
    // if(!mealJson.isMissingNode())
    // success = true;
    // i++;
    // }
    // openaiApiService.setBestofN(prevBestOfN);
    // }

    // // Convert the JSON node to a List<JsonNode> to filter the entities
    // List<JsonNode> mealList = new ArrayList<>();
    // if (mealJson.isArray()) {
    // for (JsonNode entity : mealJson) {
    // mealList.add(entity);
    // }
    // }

    // // Split the query into individual words
    // String[] searchWords = query.toLowerCase().split(" ");

    // // Filter the entities based on the query parameter
    // List<JsonNode> filteredEntities = new ArrayList<>();
    // for (JsonNode entity : mealList) {
    // String name = entity.get("name").asText().toLowerCase();
    // // String description = entity.get("description").asText().toLowerCase();
    // String ingredients = entity.get("ingredients").asText().toLowerCase();
    // String description = entity.get("description").asText().toLowerCase();
    // // String instructions = entity.get("instruction").asText().toLowerCase();

    // // Check if all search words are present in the name, ingredients, or
    // description
    // boolean allWordsFound = true;
    // for (String word : searchWords) {
    // if (!name.contains(word) && !ingredients.contains(word) &&
    // !description.contains(word)) {
    // allWordsFound = false;
    // break;
    // }
    // }
    // if (allWordsFound) {
    // filteredEntities.add(entity);
    // }

    // }
    // // if (name.contains(query.toLowerCase()) ||
    // ingredients.contains(query.toLowerCase()) ||
    // description.contains(query.toLowerCase()) ) {
    // // filteredEntities.add(entity);
    // // }
    // // }
    // // Create a new JSON array node to store the filtered entities
    // ArrayNode filteredEntitiesArray = JsonNodeFactory.instance.arrayNode();
    // filteredEntities.forEach(filteredEntitiesArray::add);

    // return filteredEntitiesArray.toString();

    // // int i = 0;
    // // JsonNode searchedMeal =
    // objectMapper.readTree(openaiApiService.fetchMealResponse(query));
    // // if (searchedMeal.isMissingNode()) {
    // // int prevBestOfN = openaiApiService.getBestofN();
    // // boolean success = false;
    // // openaiApiService.setBestofN(prevBestOfN + 1);
    // // while (!success && i < 5) {
    // // searchedMeal =
    // objectMapper.readTree(openaiApiService.fetchMealResponse(query));
    // // if (!searchedMeal.isMissingNode())
    // // success = true;
    // // i++;
    // // }
    // // openaiApiService.setBestofN(prevBestOfN);
    // // }
    // // return searchedMeal.toString();

    // }

}
