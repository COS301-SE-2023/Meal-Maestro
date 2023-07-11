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
import fellowship.mealmaestro.services.auth.JwtService;

@Repository
public class PantryRepository {

    @Autowired
    private final Driver driver;

    @Autowired
    private final JwtService jwtService;

    public PantryRepository(Driver driver, JwtService jwtService){
        this.driver = driver;
        this.jwtService = jwtService;
    }

    //#region Create
    public FoodModel addToPantry(PantryRequestModel pantryRequest){
        FoodModel food = pantryRequest.getFood();
        String email = jwtService.extractUserEmail(pantryRequest.getToken());
        try (Session session = driver.session()){
            return session.executeWrite(addToPantryTransaction(food, email));
        }
    }

    public static TransactionCallback<FoodModel> addToPantryTransaction(FoodModel food, String email){
        return transaction -> {
            transaction.run("MATCH (User{email: $email})-[:HAS_PANTRY]->(p:Pantry) \r\n" + //
                        "CREATE (p)-[:IN_PANTRY]->(:Food {name: $name, quantity: $quantity, weight: $weight})",

            Values.parameters("email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "weight", food.getWeight()));
            FoodModel addedFood = new FoodModel(food.getName(), food.getQuantity(), food.getWeight());
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
     * "token": "secretToken"
     * }
     */
    //#endregion

    //#region Read
    public List<FoodModel> getPantry(String token){
        try (Session session = driver.session()){
            return session.executeRead(getPantryTransaction(jwtService.extractUserEmail(token)));
        }
    }

    public static TransactionCallback<List<FoodModel>> getPantryTransaction(String email){
        return transaction -> {
            var result = transaction.run("MATCH (User{email: $email})-[:HAS_PANTRY]->(p:Pantry)-[:IN_PANTRY]->(f:Food) \r\n" + //
                        "RETURN f.name AS name, f.quantity AS quantity, f.weight AS weight",
            Values.parameters("email", email));

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
     * "token": "secretToken"
     * }
     */
    //#endregion

    //#region Update
    public void updatePantry(PantryRequestModel pantryRequest){
        FoodModel food = pantryRequest.getFood();
        String email = jwtService.extractUserEmail(pantryRequest.getToken());
        try (Session session = driver.session()){
            session.executeWrite(updatePantryTransaction(food, email));
        }
    }

    public static TransactionCallback<Void> updatePantryTransaction(FoodModel food, String email){
        return transaction -> {
            transaction.run("MATCH (User{email: $email})-[:HAS_PANTRY]->(p:Pantry)-[:IN_PANTRY]->(f:Food {name: $name}) \r\n" + //
                        "SET f.quantity = $quantity, f.weight = $weight",
            Values.parameters("email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "weight", food.getWeight()));
            return null;
        };
    }
    //#endregion

    //#region Delete
    public void removeFromPantry(PantryRequestModel pantryRequest){
        FoodModel food = pantryRequest.getFood();
        String email = jwtService.extractUserEmail(pantryRequest.getToken());
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
