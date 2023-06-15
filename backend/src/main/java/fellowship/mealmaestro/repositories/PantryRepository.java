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
import fellowship.mealmaestro.models.PantryRequestModel;
import fellowship.mealmaestro.models.UserModel;

@Repository
public class PantryRepository {

    @Autowired
    private final Driver driver;

    public PantryRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public void addToPantry(PantryRequestModel pantryRequest){
        FoodModel food = pantryRequest.getFood();
        UserModel user = pantryRequest.getUser();
        try (Session session = driver.session()){
            session.executeWrite(addToPantryTransaction(food, user.getUsername(), user.getEmail()));
        }
    }

    public static TransactionCallback<Void> addToPantryTransaction(FoodModel food, String username, String email){
        return transaction -> {
            transaction.run("MATCH (User{username: $username, email: $email})-[:HAS_PANTRY]->(p:Pantry) \r\n" + //
                        "CREATE (p)-[:IN_PANTRY]->(:Food {name: $name, quantity: $quantity, weight: $weight})",

            Values.parameters("username", username, "email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "weight", food.getWeight()));
            return null;
        };
    }
    /*  Example Post data:
     * {
     * "food": {
     *  "name": "Carrot",
     *  "quantity": "17",
     *  "weight": "42"
     * },
     * "user": {
     *  "username": "Frank",
     *  "email": "test@example.com"
     *  }
     * }
     */
    //#endregion

    //#region Read
    public List<FoodModel> getPantry(UserModel user){
        try (Session session = driver.session()){
            return session.executeRead(getPantryTransaction(user.getUsername(), user.getEmail()));
        }
    }

    public static TransactionCallback<List<FoodModel>> getPantryTransaction(String username, String email){
        return transaction -> {
            var result = transaction.run("MATCH (User{username: $username, email: $email})-[:HAS_PANTRY]->(p:Pantry)-[:IN_PANTRY]->(f:Food) \r\n" + //
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
    public void updatePantry(PantryRequestModel pantryRequest){
        FoodModel food = pantryRequest.getFood();
        UserModel user = pantryRequest.getUser();
        try (Session session = driver.session()){
            session.executeWrite(updatePantryTransaction(food, user.getUsername(), user.getEmail()));
        }
    }

    public static TransactionCallback<Void> updatePantryTransaction(FoodModel food, String username, String email){
        return transaction -> {
            transaction.run("MATCH (User{username: $username, email: $email})-[:HAS_PANTRY]->(p:Pantry)-[:IN_PANTRY]->(f:Food {name: $name}) \r\n" + //
                        "SET f.quantity = $quantity, f.weight = $weight",
            Values.parameters("username", username, "email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "weight", food.getWeight()));
            return null;
        };
    }
    //#endregion

    //#region Delete
    public void removeFromPantry(PantryRequestModel pantryRequest){
        FoodModel food = pantryRequest.getFood();
        UserModel user = pantryRequest.getUser();
        try (Session session = driver.session()){
            session.executeWrite(removeFromPantryTransaction(food, user.getUsername(), user.getEmail()));
        }
    }

    public static TransactionCallback<Void> removeFromPantryTransaction(FoodModel food, String username, String email){
        return transaction -> {
            transaction.run("MATCH (User{username: $username, email: $email})-[:HAS_PANTRY]->(p:Pantry)-[r:IN_PANTRY]->(f:Food {name: $name}) \r\n" + //
                        "DELETE r,f",
            Values.parameters("username", username, "email", email, "name", food.getName()));
            return null;
        };
    }
    //#endregion
}
