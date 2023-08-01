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
            transaction.run("MATCH (user:User {email: $email}), (recipe:Meal {name: $name, description: $desc, image: $image, ingredients: $ing, " +
            "instructions: $ins, cookingTime: $ck})" +
            "MERGE (user)-[:HAS_RECIPE_BOOK]->(recipeBook:`Recipe Book`) " +
            "MERGE (recipeBook)-[:CONTAINS]->(recipe)",
            Values.parameters("email", email, "name", recipe.getName(), "desc", recipe.getdescription(), "image", recipe.getimage(),
            "ing", recipe.getingredients(), "ins", recipe.getinstructions(), "ck", recipe.getcookingTime()));
            return (new MealModel(recipe.getName(), recipe.getinstructions(), recipe.getdescription(), recipe.getimage(), recipe.getingredients(), recipe.getcookingTime()));
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
            "RETURN recipe.name AS name, recipe.image AS image, recipe.description AS description, recipe.ingredients as ingredients, recipe.instructions as instructions, recipe.cookingTime as cookingTime",
            Values.parameters("email", user));
            
            List<MealModel> recipes = new ArrayList<>();
            while (result.hasNext()){
                var record = result.next();
                recipes.add(new MealModel(record.get("name").asString(), record.get("instructions").asString(), record.get("description").asString(), record.get("image").asString(),
                record.get("ingredients").asString(), record.get("cookingTime").asString()));
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
            transaction.run("MATCH (user:User {email: $email})-[:HAS_RECIPE_BOOK]->(book:`Recipe Book`)-[r:CONTAINS]->(recipe:Meal {name: $name}) " +
            "DELETE r",
                Values.parameters("email", email, "name", recipe.getName()));
            return null;
        };
    }
    //#endregion
}