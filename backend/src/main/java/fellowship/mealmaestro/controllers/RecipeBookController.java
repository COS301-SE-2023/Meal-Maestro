package fellowship.mealmaestro.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.services.RecipeBookService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
public class RecipeBookController {

    private final RecipeBookService recipeBookService;

    public RecipeBookController(RecipeBookService recipeBookService) {
        this.recipeBookService = recipeBookService;
    }

    @PostMapping("/addRecipe")
    public ResponseEntity<MealModel> addRecipe(@Valid @RequestBody MealModel request, @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String authToken = token.substring(7);
        return ResponseEntity.ok(recipeBookService.addRecipe(request, authToken));
    }

    @PostMapping("/removeRecipe")
    public ResponseEntity<Void> removeRecipe(@Valid @RequestBody MealModel request, @RequestHeader("Authorization") String token) {
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
