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
import fellowship.mealmaestro.services.auth.JwtService;

@Repository
public class ShoppingListRepository {
    
    @Autowired
    private final Driver driver;

    @Autowired
    private final JwtService jwtService;

    public ShoppingListRepository(Driver driver, JwtService jwtService){
        this.driver = driver;
        this.jwtService = jwtService;
    }

    //#region Create
    public FoodModel addToShoppingList(ShoppingListRequestModel request){
        FoodModel food = request.getFood();
        String email = jwtService.extractUserEmail(request.getToken());
        try (Session session = driver.session()){
            return session.executeWrite(addToShoppingListTransaction(food, email));
        }
    }

    public static TransactionCallback<FoodModel> addToShoppingListTransaction(FoodModel food, String email){
        return transaction -> {
            transaction.run("MATCH (User{email: $email})-[:HAS_LIST]->(s:`Shopping List`) \r\n" + //
                        "CREATE (s)-[:IN_LIST]->(:Food {name: $name, quantity: $quantity, weight: $weight})",

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
     *  "quantity": "0",
     *  "weight": "0"
     * },
     * "token": "secretToken"
     * }
     */
    //#endregion

    //#region Read
    public List<FoodModel> getShoppingList(String token){
        try (Session session = driver.session()){
            return session.executeRead(getShoppingListTransaction(jwtService.extractUserEmail(token)));
        }
    }

    public static TransactionCallback<List<FoodModel>> getShoppingListTransaction(String email){
        return transaction -> {
            var result = transaction.run("MATCH (User{email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[:IN_LIST]->(f:Food) \r\n" + //
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
    public void updateShoppingList(ShoppingListRequestModel request){
        FoodModel food = request.getFood();
        String email = jwtService.extractUserEmail(request.getToken());
        try (Session session = driver.session()){
            session.executeWrite(updateShoppingListTransaction(food, email));
        }
    }

    public static TransactionCallback<Void> updateShoppingListTransaction(FoodModel food, String email){
        return transaction -> {
            transaction.run("MATCH (User{email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[:IN_LIST]->(f:Food {name: $name}) \r\n" + //
                        "SET f.quantity = $quantity, f.weight = $weight",

            Values.parameters("email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "weight", food.getWeight()));
            return null;
        };
    }
    //#endregion

    //#region Delete
    public void removeFromShoppingList(ShoppingListRequestModel request){
        FoodModel food = request.getFood();
        String email = jwtService.extractUserEmail(request.getToken());
        try (Session session = driver.session()){
            session.executeWrite(removeFromShoppingListTransaction(food, email));
        }
    }

    public static TransactionCallback<Void> removeFromShoppingListTransaction(FoodModel food, String email){
        return transaction -> {
            transaction.run("MATCH (User{email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[r:IN_LIST]->(f:Food {name: $name}) \r\n" + //
                        "DELETE r,f",

            Values.parameters("email", email, "name", food.getName()));
            return null;
        };
    }
    //#endregion
}
