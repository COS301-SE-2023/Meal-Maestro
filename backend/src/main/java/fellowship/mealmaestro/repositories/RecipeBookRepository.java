package fellowship.mealmaestro.repositories;

import org.springframework.stereotype.Repository;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.neo4j.driver.Values;

import java.util.List;
import java.util.ArrayList;

import fellowship.mealmaestro.models.RecipeModel;
import fellowship.mealmaestro.models.UserModel;

@Repository
public class RecipeBookRepository {
    
    @Autowired
    private final Driver driver;

    public RecipeBookRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public void addRecipe(UserModel user, RecipeModel recipe){
        try (Session session = driver.session()){
            session.executeWrite(addRecipeTransaction(user, recipe));
        }
    }

    public static TransactionCallback<Void> addRecipeTransaction(UserModel user, RecipeModel recipe) {
        return transaction -> {
            transaction.run("MATCH (user:User {email: $email}), (recipe:Recipe {title: $title})" +
            "MERGE (user)-[:HAS_RECIPE_BOOK]->(recipeBook:Recipe Book) " +
            "MERGE (recipeBook)-[:CONTAINS]->(recipe)",
            Values.parameters("email", user.getEmail(), "title", recipe.getTitle()));
            return null;
        };
    }
    //#endregion

    //#region Read               
    public List<RecipeModel> getAllRecipes(String user){
        try (Session session = driver.session()){
            return session.executeRead(getAllRecipesTransaction(user));
        }
    }

    public static TransactionCallback<List<RecipeModel>> getAllRecipesTransaction(String user) { System.out.println(user);
        return transaction -> {
            var result = transaction.run("MATCH (user:User {email: $email})-[:HAS_RECIPE_BOOK]->(book:`Recipe Book`)-[:CONTAINS]->(recipe:Recipe) " +
            "RETURN recipe.title AS title, recipe.image AS image",
            Values.parameters("email", user));
            
            List<RecipeModel> recipes = new ArrayList<>();
            while (result.hasNext()){
                var record = result.next();
                recipes.add(new RecipeModel(record.get("title").asString(), record.get("image").asString()));
            }
            return recipes;
        };
    }
    //#endregion

    //#region Delete
    public void removeRecipe(RecipeModel recipe, String email){
        try (Session session = driver.session()){
            session.executeWrite(removeRecipeTransaction(recipe, email));
        }
    }

    public static TransactionCallback<Void> removeRecipeTransaction(RecipeModel recipe, String email) {
        return transaction -> {
            transaction.run("MATCH (user:User {email: $email})-[:HAS_RECIPE_BOOK]->(book:`Recipe Book`)-[r:CONTAINS]->(recipe:Recipe {title: $title}) " +
            "DELETE r",
                Values.parameters("email", email, "title", recipe.getTitle()));
            return null;
        };
    }
    //#endregion
}