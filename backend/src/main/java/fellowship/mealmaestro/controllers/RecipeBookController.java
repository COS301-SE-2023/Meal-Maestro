import org.springframework.web.bind.annotation.*;

import fellowship.mealmaestro.models.RecipeModel;
import fellowship.mealmaestro.models.UserModel;

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
    public List<RecipeModel> getAllRecipes(@RequestBody UserModel user) {
        return recipeBookService.getAllRecipes(user);
    }
}
