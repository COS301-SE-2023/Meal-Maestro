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
                        "CREATE (s)-[:IN_LIST]->(:Food {name: $name, quantity: $quantity, unit: $unit})",

            Values.parameters("email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "unit", food.getUnit()));

            FoodModel addedFood = new FoodModel(food.getName(), food.getQuantity(), food.getUnit());
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
    public void updateShoppingList(FoodModel food, String email){
        try (Session session = driver.session()){
            session.executeWrite(updateShoppingListTransaction(food, email));
        }
    }

    public static TransactionCallback<Void> updateShoppingListTransaction(FoodModel food, String email){
        return transaction -> {
            transaction.run("MATCH (User{email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[:IN_LIST]->(f:Food {name: $name}) \r\n" + //
                        "SET f.quantity = $quantity, f.unit = $unit",

            Values.parameters("email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "unit", food.getUnit()));
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
                var unitResult = transaction.run(
                    // If food exists in both, update the pantry food and delete the shopping list food
                    "MATCH (u:User{email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[r:IN_LIST]->(f:Food {name: $name}), \r\n" + //
                    "(u)-[:HAS_PANTRY]->(p:`Pantry`)-[:IN_PANTRY]->(fp:Food{name: $name}) \r\n" + //
                    "SET fp.unit = fp.unit, fp.quantity = fp.quantity + f.quantity \r\n" + //
                    "WITH fp.unit AS unit, f.quantity AS quantity, fp.quantity AS total, r, f \r\n" + //
                    "DELETE r, f \r\n" +
                    "RETURN unit, quantity, total",
                    Values.parameters("email", email, "name", food.getName())
                );
                // if food unit is different, convert the shopping list food to the pantry food unit
                var record = unitResult.single();
                String unit = record.get("unit").asString();
                double quantity = record.get("quantity").asDouble();
                double total = record.get("total").asDouble();
                if (!unit.equals(food.getUnit())){
                    total = total - quantity;
                    if (unit.equals("g") && food.getUnit().equals("kg")){
                        total = total + (quantity * 1000);
                    } else if (unit.equals("kg") && food.getUnit().equals("g")){
                        total = total + (quantity / 1000);
                    } else if (unit.equals("ml") && food.getUnit().equals("l")){
                        total = total + (quantity * 1000);
                    } else if (unit.equals("l") && food.getUnit().equals("ml")){
                        total = total + (quantity / 1000);
                    } else {

                        throw new IllegalArgumentException("Cannot convert " + unit + " to " + food.getUnit());
                    }
                }

                if (total >= 1000 && (unit.equals("g") || unit.equals("ml"))){
                    unit = unit.equals("g") ? "kg" : "l";
                    total = total / 1000;
                } else if (total < 1 && (unit.equals("kg") || unit.equals("l"))){
                    unit = unit.equals("kg") ? "g" : "ml";
                    total = total * 1000;
                }

                transaction.run(
                    "MATCH (u:User{email: $email})-[:HAS_PANTRY]->(p:`Pantry`)-[:IN_PANTRY]->(fp:Food{name: $name}) \r\n" + //
                    "SET fp.quantity = $total, fp.unit = $unit",
                    Values.parameters("email", email, "name", food.getName(), "total", total, "unit", unit)
                );

            } else {
                transaction.run(
                    // If food only exists in shopping list, create it in the pantry and delete it from the shopping list
                    "MATCH (u:User{email: $email})-[:HAS_LIST]->(s:`Shopping List`)-[r:IN_LIST]->(f:Food {name: $name}), \r\n" + //
                    "(u)-[:HAS_PANTRY]->(p:`Pantry`) \r\n" + //
                    "CREATE (p)-[:IN_PANTRY]->(:Food {name: $name, quantity: f.quantity, unit: f.unit}) \r\n" + //
                    "DELETE r, f",
                    Values.parameters("email", email, "name", food.getName())
                );
            }

            var result = transaction.run(
                "MATCH (u:User{email: $email})-[:HAS_PANTRY]->(:`Pantry`)-[:IN_PANTRY]->(f:Food) RETURN f.name AS name, f.quantity AS quantity, f.unit AS unit \r\n", //
                Values.parameters("email", email)
            );

            List<FoodModel> foods = new ArrayList<>();
            while (result.hasNext()){
                var record = result.next();
                foods.add(new FoodModel(record.get("name").asString(), record.get("quantity").asDouble(), record.get("unit").asString()));
            }
            return foods;
        };
    }
}
