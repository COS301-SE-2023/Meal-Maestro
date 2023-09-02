package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.models.RecipeBookModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.repositories.RecipeBookRepository;
import fellowship.mealmaestro.repositories.UserRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class RecipeBookService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RecipeBookRepository recipeBookRepository;

    @Autowired
    private UserRepository userRepository;

    public MealModel addRecipe(MealModel recipe, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        RecipeBookModel recipeBook = user.getRecipeBook();

        recipeBook.getRecipes().add(recipe);
        recipeBookRepository.save(recipeBook);

        return recipe;
    }

    public void removeRecipe(MealModel request, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        RecipeBookModel recipeBook = user.getRecipeBook();

        if (recipeBook.getRecipes() == null) {
            return;
        }

        recipeBook.getRecipes().removeIf(r -> r.getName().equals(request.getName()));
        recipeBookRepository.save(recipeBook);
    }

    public List<MealModel> getAllRecipes(String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        RecipeBookModel recipeBook = user.getRecipeBook();

        return recipeBook.getRecipes();
    }
}