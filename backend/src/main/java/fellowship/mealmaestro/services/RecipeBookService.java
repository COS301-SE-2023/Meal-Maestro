package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import fellowship.mealmaestro.models.MealModel;
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

    public MealModel addRecipe(MealModel recipe, String token) {
        String email = jwtService.extractUserEmail(token);
        return recipeBookRepository.addRecipe(recipe, email);
    }

    public void removeRecipe(MealModel request, String token) {
        String email = jwtService.extractUserEmail(token);
        recipeBookRepository.removeRecipe(request, email);
    }

    public List<MealModel> getAllRecipes(String token) {
        String email = jwtService.extractUserEmail(token);

        return recipeBookRepository.getAllRecipes(email);
    }
}