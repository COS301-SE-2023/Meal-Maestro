package fellowship.mealmaestro.services;

import org.springframework.stereotype.Service;

import java.util.List;

import fellowship.mealmaestro.models.RecipeModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.repositories.RecipeBookRepository;

@Service
public class RecipeBookService {
    
    private final RecipeBookRepository recipeBookRepository;

    public RecipeBookService(RecipeBookRepository recipeBookRepository) {
        this.recipeBookRepository = recipeBookRepository;
    }

    public void addRecipe(UserModel user, RecipeModel recipe) {
        recipeBookRepository.addRecipe(user, recipe);
    }

    public void removeRecipe(UserModel user, RecipeModel recipe) {
        recipeBookRepository.removeRecipe(user, recipe);
    }

    public List<RecipeModel> getAllRecipes(UserModel user) {
        return recipeBookRepository.getAllRecipes(user);
    }
}