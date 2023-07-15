import org.springframework.stereotype.Service;

@Service
public class RecipeBookService {
    
    private final RecipeBookRepository recipeBookRepository;

    public RecipeBookService(RecipeBookRepository recipeBookRepository) {
        this.recipeBookRepository = recipeBookRepository;
    }

    public RecipeModel addRecipe(RecipeModel recipe) {
        return recipeBookRepository.addRecipe(recipe);
    }

    public void removeRecipe(RecipeModel recipe) {
        recipeBookRepository.removeRecipe(recipe);
    }

    public List<RecipeModel> getAllRecipes() {
        return recipeBookRepository.getAllRecipes();
    }

    public RecipeModel getRecipeById(String recipeId) {
        return recipeBookRepository.getRecipeById(recipeId);
    }
}
