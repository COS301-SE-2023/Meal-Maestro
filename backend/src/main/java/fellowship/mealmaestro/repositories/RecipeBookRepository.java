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
            transaction.run("MATCH (user:User {email: $email})" +
            "CREATE (user)-[:]"
            "CREATE (:RecipeBook)-[:CONTAINS]->(:Recipe {title: $title, image: $image})",
                Values.parameters("title", recipe.getTitle(), "image", recipe.getImage()));
            return null;
        };
    }
    //#endregion

    //#region Read               UPDATE!
    public List<RecipeModel> getAllRecipes(UserModel user){
        try (Session session = driver.session()){
            return session.executeRead(getAllRecipesTransaction(user));
        }
    }

    public static TransactionCallback<List<RecipeModel>> getAllRecipesTransaction(UserModel user) {
        return transaction -> {
            var result = transaction.run("MATCH (user:User {email: $email})-[:OWNS]->(book:RecipeBook)-[:CONTAINS]->(recipe:Recipe) " +
            "RETURN recipe.title AS title, recipe.image AS image",
            Values.parameters("email", user.getEmail()));
            
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
    public void removeRecipe(RecipeModel recipeName){
        try (Session session = driver.session()){
            session.executeWrite(removeRecipeTransaction(recipeName));
        }
    }

    public static TransactionCallback<Void> removeRecipeTransaction(RecipeModel recipeName) {
        return transaction -> {
            transaction.run("MATCH (user:User {email: $email})-[:OWNS]->(book:RecipeBook)-[r:CONTAINS]->(recipe:Recipe {title: $title, image: $image}) " +
            "DETACH DELETE r",
                Values.parameters("email", user.getEmail(), "title", recipe.getTitle(), "image", recipe.getImage()));
            return null;
        };
    }
    //#endregion
}
