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

@Repository
public class PantryRepository {

    @Autowired
    private final Driver driver;

    public PantryRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public FoodModel addToPantry(FoodModel food, String email){
        try (Session session = driver.session()){
            return session.executeWrite(addToPantryTransaction(food, email));
        }
    }

    public static TransactionCallback<FoodModel> addToPantryTransaction(FoodModel food, String email){
        return transaction -> {
            transaction.run("MATCH (User{email: $email})-[:HAS_PANTRY]->(p:Pantry) \r\n" + //
                        "CREATE (p)-[:IN_PANTRY]->(:Food {name: $name, quantity: $quantity, unit: $unit})",

            Values.parameters("email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "unit", food.getUnit()));
            FoodModel addedFood = new FoodModel(food.getName(), food.getQuantity(), food.getUnit());
            return addedFood;
        };
    }
    /*  Example Post data:
     * {
     * "food": {
     *  "name": "Carrot",
     *  "quantity": "17",
     *  "weight": "42"
     * },
     * }
     */
    //#endregion

    //#region Read
    public List<FoodModel> getPantry(String email){
        try (Session session = driver.session()){
            return session.executeRead(getPantryTransaction(email));
        }
    }

    public static TransactionCallback<List<FoodModel>> getPantryTransaction(String email){
        return transaction -> {
            var result = transaction.run("MATCH (User{email: $email})-[:HAS_PANTRY]->(p:Pantry)-[:IN_PANTRY]->(f:Food) \r\n" + //
                        "RETURN f.name AS name, f.quantity AS quantity, f.unit AS unit",
            Values.parameters("email", email));

            List<FoodModel> foods = new ArrayList<>();
            while (result.hasNext()){
                var record = result.next();
                foods.add(new FoodModel(record.get("name").asString(), record.get("quantity").asDouble(), record.get("unit").asString()));
            }
            return foods;
        };
    }
    /*  Example Post data:
     * {
     * }
     */
    //#endregion

    //#region Update
    public void updatePantry(FoodModel food, String email){
        try (Session session = driver.session()){
            session.executeWrite(updatePantryTransaction(food, email));
        }
    }

    public static TransactionCallback<Void> updatePantryTransaction(FoodModel food, String email){
        return transaction -> {
            transaction.run("MATCH (User{email: $email})-[:HAS_PANTRY]->(p:Pantry)-[:IN_PANTRY]->(f:Food {name: $name}) \r\n" + //
                        "SET f.quantity = $quantity, f.unit = $unit",
            Values.parameters("email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "unit", food.getUnit()));
            return null;
        };
    }
    //#endregion

    //#region Delete
    public void removeFromPantry(FoodModel food, String email){
        try (Session session = driver.session()){
            session.executeWrite(removeFromPantryTransaction(food, email));
        }
    }

    public static TransactionCallback<Void> removeFromPantryTransaction(FoodModel food, String email){
        return transaction -> {
            transaction.run("MATCH (User{email: $email})-[:HAS_PANTRY]->(p:Pantry)-[r:IN_PANTRY]->(f:Food {name: $name}) \r\n" + //
                        "DELETE r,f",
            Values.parameters("email", email, "name", food.getName()));
            return null;
        };
    }
    //#endregion
}
