package fellowship.mealmaestro.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fellowship.mealmaestro.models.RecipeModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.services.RecipeBookService;

import java.util.List;

@RestController
public class RecipeBookController {

    private final RecipeBookService recipeBookService;

    public RecipeBookController(RecipeBookService recipeBookService) {
        this.recipeBookService = recipeBookService;
    }

    @PostMapping("/addRecipe")
    public void addRecipe(@RequestBody UserModel user, @RequestBody RecipeModel recipe) {
        recipeBookService.addRecipe(user, recipe);
    }

    @PostMapping("/removeRecipe")
    public void removeRecipe(@RequestBody UserModel user, @RequestBody RecipeModel recipe) {
        recipeBookService.removeRecipe(user, recipe);
    }

    @PostMapping("/getAllRecipes")
    public ResponseEntity<List<RecipeModel>> getAllRecipes(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String authToken = token.substring(7);
        return ResponseEntity.ok(recipeBookService.getAllRecipes(authToken));
    }
}
