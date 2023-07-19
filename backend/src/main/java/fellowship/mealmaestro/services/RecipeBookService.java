import org.springframework.stereotype.Service;

import java.util.List;

import fellowship.mealmaestro.models.RecipeModel;
import fellowship.mealmaestro.models.UserModel;

@Service
public class RecipeBookService {
    
    private final RecipeBookRepository recipeBookRepository;

    public RecipeBookService(RecipeBookRepository recipeBookRepository) {
        this.recipeBookRepository = recipeBookRepository;
    }

    public void addRecipe(RecipeModel recipe) {
        recipeBookRepository.addRecipe(recipe);
    }

    public void removeRecipe(RecipeModel recipe) {
        recipeBookRepository.removeRecipe(recipe);
    }

    public List<RecipeModel> getAllRecipes(UserModel user) {
        return recipeBookRepository.getAllRecipes(user);
    }
}
