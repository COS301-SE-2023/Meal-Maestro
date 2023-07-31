package fellowship.mealmaestro.repositories;

import org.springframework.stereotype.Repository;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.neo4j.driver.Values;

import java.util.List;
import java.util.ArrayList;

import fellowship.mealmaestro.models.MealModel;

@Repository
public class RecipeBookRepository {
    
    @Autowired
    private final Driver driver;

    public RecipeBookRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public MealModel addRecipe(MealModel recipe, String email){
        try (Session session = driver.session()){
           return session.executeWrite(addRecipeTransaction(recipe, email));
        }
    }

    public static TransactionCallback<MealModel> addRecipeTransaction(MealModel recipe, String email) {
        return transaction -> {
            transaction.run("MATCH (user:User {email: $email}), (recipe:Meal {title: $title})" +
            "MERGE (user)-[:HAS_RECIPE_BOOK]->(recipeBook:`Recipe Book`) " +
            "MERGE (recipeBook)-[:CONTAINS]->(recipe)",
            Values.parameters("email", email, "title", recipe.getName()));
            return (new MealModel(recipe.getName(), recipe.getimage()));
        };
    }
    //#endregion

    //#region Read               
    public List<MealModel> getAllRecipes(String user){
        try (Session session = driver.session()){
            return session.executeRead(getAllRecipesTransaction(user));
        }
    }

    public static TransactionCallback<List<MealModel>> getAllRecipesTransaction(String user) {
        return transaction -> {
            var result = transaction.run("MATCH (user:User {email: $email})-[:HAS_RECIPE_BOOK]->(book:`Recipe Book`)-[:CONTAINS]->(recipe:Meal) " +
            "RETURN recipe.title AS title, recipe.image AS image",
            Values.parameters("email", user));
            
            List<MealModel> recipes = new ArrayList<>();
            while (result.hasNext()){
                var record = result.next();
                recipes.add(new MealModel(record.get("title").asString(), record.get("image").asString()));
            }
            return recipes;
        };
    }
    //#endregion

    //#region Delete
    public void removeRecipe(MealModel recipe, String email){
        try (Session session = driver.session()){
            session.executeWrite(removeRecipeTransaction(recipe, email));
        }
    }

    public static TransactionCallback<Void> removeRecipeTransaction(MealModel recipe, String email) {
        return transaction -> {
            transaction.run("MATCH (user:User {email: $email})-[:HAS_RECIPE_BOOK]->(book:`Recipe Book`)-[r:CONTAINS]->(recipe:Meal {title: $title}) " +
            "DELETE r",
                Values.parameters("email", email, "title", recipe.getTitle()));
            return null;
        };
    }
    //#endregion
}