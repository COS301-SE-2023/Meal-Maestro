package fellowship.mealmaestro.repositories;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fellowship.mealmaestro.models.SettingsModel;

@Repository
public class SettingsRepository {

    @Autowired
    private final Driver driver;

    public SettingsRepository(Driver driver){
        this.driver = driver;
    }

    public List<SettingsModel> getSettings(String email){
        try (Session session = driver.session()){
            return session.executeRead(getSettingsTransaction(email));
        }
    }

    public static TransactionCallback<SettingsModel> addToPantryTransaction(SettingsModel food, String email){
        return transaction -> {
            transaction.run("MATCH (User{email: $email})-[:HAS_PANTRY]->(p:Pantry) \r\n" + //
                        "CREATE (p)-[:IN_PANTRY]->(:Food {name: $name, quantity: $quantity, weight: $weight})",
    
            Values.parameters("email", email, "name", food.getName(),
                        "quantity", food.getQuantity(), "weight", food.getWeight()));
            SettingsModel addedFood = new SettingsModel(food.getName(), food.getQuantity(), food.getWeight());
            return addedFood;
        };
    }
    
}
