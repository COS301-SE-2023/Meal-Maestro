package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.services.BrowseService;
import fellowship.mealmaestro.services.MealManagementService;
import jakarta.validation.Valid;

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

    // @GetMapping("/getPopularMeals")
    // public ResponseEntity<List<MealModel>> getPopularMeals(@RequestHeader("Authorization") String token){
    //     if (token == null || token.isEmpty()) {
    //         return ResponseEntity.badRequest().build();
    //     }
    //     String authToken = token.substring(7);
    //     return ResponseEntity.ok(BrowseService.getPopularMeals(authToken));
    // }

    // @GetMapping("/getSearchedMeals")
    // public ResponseEntity<List<MealModel>> searchMeals(@Valid @RequestBody MealModel request, @RequestHeader("Authorization") String token){
    //     if (token == null || token.isEmpty()) {
    //         return ResponseEntity.badRequest().build();
    //     }
    //     String authToken = token.substring(7);
    //     return ResponseEntity.ok(BrowseService.getSearchedMeals                                                                     (authToken));
    // }

    // @GetMapping("/getPopularMeals")
    // public String popularMeals() throws JsonMappingException, JsonProcessingException{
    //     return mealManagementService.generatePopularMeals();
    // }

    // @GetMapping("/getSearchedMeals")
    // public String searchedMeals(@RequestParam String query) throws JsonMappingException, JsonProcessingException {
    //     // Call the mealManagementService to search meals based on the query
    //     return mealManagementService.generateSearchedMeals(query);
    // }
 }
