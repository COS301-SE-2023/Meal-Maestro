package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import fellowship.mealmaestro.models.RecipeModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.repositories.RecipeBookRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class RecipeBookService {

    @Autowired
    private JwtService jwtService;
    
    private final RecipeBookRepository recipeBookRepository;

    public RecipeBookService(RecipeBookRepository recipeBookRepository) {
        this.recipeBookRepository = recipeBookRepository;
    }

    public RecipeModel addRecipe(RecipeModel recipe, String token) {
        String email = jwtService.extractUserEmail(token);
        recipeBookRepository.addRecipe(recipe, email);
    }

    public void removeRecipe(RecipeModel request, String token) {
        String email = jwtService.extractUserEmail(token);
        recipeBookRepository.removeRecipe(request, email);
    }

    public List<RecipeModel> getAllRecipes(String token) {
        String email = jwtService.extractUserEmail(token);

        return recipeBookRepository.getAllRecipes(email);
    }
}