import org.springframework.stereotype.Service;

import java.util.List;

import fellowship.mealmaestro.models.RecipeModel;

@Service
public class RecipeBookService {
    
    private final RecipeBookRepository recipeBookRepository;

    public RecipeBookService(RecipeBookRepository recipeBookRepository) {
        this.recipeBookRepository = recipeBookRepository;
    }

    public void addRecipe(RecipeModel recipe) {
        return recipeBookRepository.addRecipe(recipe);
    }

    public void removeRecipe(RecipeModel recipe) {
        recipeBookRepository.removeRecipe(recipe);
    }

    public List<RecipeModel> getAllRecipes() {
        return recipeBookRepository.getAllRecipes();
    }
}
