package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.services.BrowseService;

@RestController
public class BrowseController {

    private final BrowseService browseService;

    public BrowseController(BrowseService browseService) {
        this.browseService = browseService;
    }

    @GetMapping("/getPopularMeals")
    public ResponseEntity<List<MealModel>> getPopularMeals(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(browseService.getPopularMeals());
    }

    @GetMapping("/getSearchedMeals")
    public ResponseEntity<List<MealModel>> getSearcedhMeals(@RequestParam("query") String mealName,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(browseService.getSearchedMeals(mealName));
    }

}
