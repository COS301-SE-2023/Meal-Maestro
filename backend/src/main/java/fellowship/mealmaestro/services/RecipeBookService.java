package fellowship.mealmaestro.services;

import org.springframework.stereotype.Service;

import java.util.List;

import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.models.neo4j.RecipeBookModel;
import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.repositories.neo4j.RecipeBookRepository;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class RecipeBookService {

    private final JwtService jwtService;
    private final RecipeBookRepository recipeBookRepository;
    private final UserRepository userRepository;

    public RecipeBookService(JwtService jwtService, RecipeBookRepository recipeBookRepository,
            UserRepository userRepository) {
        this.jwtService = jwtService;
        this.recipeBookRepository = recipeBookRepository;
        this.userRepository = userRepository;
    }

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