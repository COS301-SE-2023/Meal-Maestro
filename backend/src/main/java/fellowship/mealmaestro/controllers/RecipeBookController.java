import org.springframework.web.bind.annotation.*;

@RestController
public class RecipeBookController {

    private final RecipeBookService recipeBookService;

    public RecipeBookController(RecipeBookService recipeBookService) {
        this.recipeBookService = recipeBookService;
    }

    @PostMapping("/addToRecipeBook")
    public void addToRecipeBook(@RequestBody RecipeModel recipe) {
        recipeBookService.addToRecipeBook(recipe);
    }

    @PostMapping("/removeFromRecipeBook")
    public void removeFromRecipeBook(@RequestBody RecipeModel recipe) {
        recipeBookService.removeFromRecipeBook(recipe);
    }

    @PostMapping("/getRecipeBook")
    public List<RecipeModel> getRecipeBook(@RequestBody UserModel user) {
        return recipeBookService.getRecipeBook(user);
    }
}
