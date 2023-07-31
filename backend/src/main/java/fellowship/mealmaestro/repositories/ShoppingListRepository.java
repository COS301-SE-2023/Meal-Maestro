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
public class ShoppingListRepository {
    
    @Autowired
    private final Driver driver;

    public ShoppingListRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public FoodModel addToShoppingList(FoodModel food, String email){
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
     *  "name": "Carrot",
     *  "quantity": "0",
     *  "weight": "0"
     * }
     */
    //#endregion

    //#region Read
    public List<FoodModel> getShoppingList(String email){
        try (Session session = driver.session()){
            return session.executeRead(getShoppingListTransaction(email));
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
     * }
     */
    //#endregion

    //#region Update
    public void updateShoppingList(FoodModel food, String email){
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
    public void removeFromShoppingList(FoodModel food, String email){
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

    //#region Move to Pantry
    public List<FoodModel> buyItem(FoodModel food, String email){
        try (Session session = driver.session()){
            return session.executeWrite(buyItemTransaction(food, email));
        }
    }

    public static TransactionCallback<List<FoodModel>> buyItemTransaction(FoodModel food, String email){
        return transaction -> {
            var findFoodInBoth = transaction.run(
                "MATCH (u:User{email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[:IN_LIST]->(:Food {name: $name}), \r\n" + //
                "(u)-[:HAS_PANTRY]->(p:`Pantry`)-[:IN_PANTRY]->(:Food {name: $name}) \r\n" + //
                "RETURN count(*)",
                Values.parameters("email", email, "name", food.getName())
            );

            boolean foodInBoth = findFoodInBoth.single().get(0).asInt() > 0;

            if (foodInBoth) {
                transaction.run(
                    // If food exists in both, update the pantry food and delete the shopping list food
                    "MATCH (u:User{email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[r:IN_LIST]->(f:Food {name: $name}), \r\n" + //
                    "(u)-[:HAS_PANTRY]->(p:`Pantry`)-[:IN_PANTRY]->(fp:Food{name: $name}) \r\n" + //
                    "SET fp.weight = fp.weight + f.weight, fp.quantity = fp.quantity + f.quantity \r\n" + //
                    "DELETE r, f",
                    Values.parameters("email", email, "name", food.getName())
                );
            } else {
                transaction.run(
                    // If food only exists in shopping list, create it in the pantry and delete it from the shopping list
                    "MATCH (u:User{email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[r:IN_LIST]->(f:Food {name: $name}), \r\n" + //
                    "(u)-[:HAS_PANTRY]->(p:`Pantry`) \r\n" + //
                    "CREATE (p)-[:IN_PANTRY]->(:Food {name: $name, quantity: f.quantity, weight: f.weight}) \r\n" + //
                    "DELETE r, f",
                    Values.parameters("email", email, "name", food.getName())
                );
            }

            var result = transaction.run(
                "MATCH (u:User{email: $email})-[:HAS_PANTRY]->(:`Pantry`)-[:IN_PANTRY]->(f:Food) RETURN f.name AS name, f.quantity AS quantity, f.weight AS weight \r\n", //
                Values.parameters("email", email)
            );

            List<FoodModel> foods = new ArrayList<>();
            while (result.hasNext()){
                var record = result.next();
                foods.add(new FoodModel(record.get("name").asString(), record.get("quantity").asInt(), record.get("weight").asInt()));
            }
            return foods;
        };
    }
}
