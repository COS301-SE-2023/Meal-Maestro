package fellowship.mealmaestro.repositories;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.ShoppingListRequestModel;
import fellowship.mealmaestro.models.UserModel;

@Repository
public class ShoppingListRepository {
    
    @Autowired
    private final Driver driver;

    public ShoppingListRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public FoodModel addToShoppingList(ShoppingListRequestModel request){
        FoodModel food = request.getFood();
        UserModel user = request.getUser();
        try (Session session = driver.session()){
            return session.executeWrite(addToShoppingListTransaction(food, user.getUsername(), user.getEmail()));
        }
    }

    public static TransactionCallback<FoodModel> addToShoppingListTransaction(FoodModel food, String username, String email){
        return transaction -> {
            transaction.run("MATCH (User{username: $username, email: $email})-[:HAS_LIST]->(s:`Shopping List`) \r\n" + //
                        "CREATE (s)-[:IN_LIST]->(:Food {name: $name, quantity: $quantity, weight: $weight})",

            Values.parameters("username", username, "email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "weight", food.getWeight()));

            FoodModel addedFood = new FoodModel(food.getName(), food.getQuantity(), food.getWeight());
            return addedFood;
        };
    }
    /*  Example Post data:
     * {
     * "food": {
     *  "name": "Carrot",
     *  "quantity": "0",
     *  "weight": "0"
     * },
     * "user": {
     *  "username": "Frank",
     *  "email": "test@example.com"
     *  }
     * }
     */
    //#endregion

    //#region Read
    public List<FoodModel> getShoppingList(UserModel user){
        try (Session session = driver.session()){
            return session.executeRead(getShoppingListTransaction(user.getUsername(), user.getEmail()));
        }
    }

    public static TransactionCallback<List<FoodModel>> getShoppingListTransaction(String username, String email){
        return transaction -> {
            var result = transaction.run("MATCH (User{username: $username, email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[:IN_LIST]->(f:Food) \r\n" + //
                        "RETURN f.name AS name, f.quantity AS quantity, f.weight AS weight",

            Values.parameters("username", username, "email", email));
            
            List<FoodModel> foods = new ArrayList<>();
            while (result.hasNext()){
                var record = result.next();
                foods.add(new FoodModel(record.get("name").asString(), record.get("quantity").asInt(), record.get("weight").asInt()));
            }
            return foods;
        };
    }
    /*  Example Post data:
     * {
     * "username": "Frank",
     * "email": "test@example.com"
     * }
     */
    //#endregion

    //#region Update
    public void updateShoppingList(ShoppingListRequestModel request){
        FoodModel food = request.getFood();
        UserModel user = request.getUser();
        try (Session session = driver.session()){
            session.executeWrite(updateShoppingListTransaction(food, user.getUsername(), user.getEmail()));
        }
    }

    public static TransactionCallback<Void> updateShoppingListTransaction(FoodModel food, String username, String email){
        return transaction -> {
            transaction.run("MATCH (User{username: $username, email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[:IN_LIST]->(f:Food {name: $name}) \r\n" + //
                        "SET f.quantity = $quantity, f.weight = $weight",

            Values.parameters("username", username, "email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "weight", food.getWeight()));
            return null;
        };
    }
    //#endregion

    //#region Delete
    public void removeFromShoppingList(ShoppingListRequestModel request){
        FoodModel food = request.getFood();
        UserModel user = request.getUser();
        try (Session session = driver.session()){
            session.executeWrite(removeFromShoppingListTransaction(food, user.getUsername(), user.getEmail()));
        }
    }

    public static TransactionCallback<Void> removeFromShoppingListTransaction(FoodModel food, String username, String email){
        return transaction -> {
            transaction.run("MATCH (User{username: $username, email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[r:IN_LIST]->(f:Food {name: $name}) \r\n" + //
                        "DELETE r,f",

            Values.parameters("username", username, "email", email, "name", food.getName()));
            return null;
        };
    }
    //#endregion
}
