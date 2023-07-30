package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.services.BrowseService;
//import fellowship.mealmaestro.services.PantryService;
import jakarta.validation.Valid;

@RestController
public class BrowseController {

    @Autowired
    private BrowseService browseService;

    @GetMapping("/getPopularMeals")
    public ResponseEntity<List<MealModel>> getPopularMeals(@RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        return ResponseEntity.ok(browseService.getPopularMeals(authToken));
    }

    @GetMapping("/getSearchedMeals")
    public ResponseEntity<List<MealModel>> searchMeals(@Valid @RequestBody String request, @RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        return ResponseEntity.ok(browseService.getSearchedMeals(request,authToken));                                                                     
    }
    
}
