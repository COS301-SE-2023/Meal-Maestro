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
    public void addRecipe(RecipeModel recipe){
        try (Session session = driver.session()){
            session.executeWrite(addRecipeTransaction(recipe));
        }
    }

    public static TransactionCallback<Void> addRecipeTransaction(RecipeModel recipe) {
        return transaction -> {
            transaction.run("CREATE (:RecipeBook)-[:CONTAINS]->(:Recipe {name: $name, description: $description})",
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
            var result = transaction.run("MATCH (:RecipeBook)-[:CONTAINS]->(r:Recipe) RETURN r.title AS title, r.image AS image");
            
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
            transaction.run("MATCH (:RecipeBook)-[:CONTAINS]->(r:Recipe {name: $name}) DETACH DELETE r",
                Values.parameters("name", recipeName));
            return null;
        };
    }
    //#endregion
}
