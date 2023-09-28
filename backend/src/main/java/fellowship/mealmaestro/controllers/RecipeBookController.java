package fellowship.mealmaestro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.services.LogService;
import fellowship.mealmaestro.services.RecipeBookService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
public class RecipeBookController {
    @Autowired
    private LogService logService;
    private final RecipeBookService recipeBookService;

    public RecipeBookController(RecipeBookService recipeBookService) {
        this.recipeBookService = recipeBookService;
    }

    @PostMapping("/addRecipe")
    public ResponseEntity<MealModel> addRecipe(@Valid @RequestBody MealModel request,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        logService.logMeal(token, request, "save");

        String authToken = token.substring(7);
        return ResponseEntity.ok(recipeBookService.addRecipe(request, authToken));
    }

    @PostMapping("/removeRecipe")
    public ResponseEntity<Void> removeRecipe(@Valid @RequestBody MealModel request,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String authToken = token.substring(7);
        recipeBookService.removeRecipe(request, authToken);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/getAllRecipes")
    public ResponseEntity<List<MealModel>> getAllRecipes(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String authToken = token.substring(7);
        return ResponseEntity.ok(recipeBookService.getAllRecipes(authToken));
    }
}
