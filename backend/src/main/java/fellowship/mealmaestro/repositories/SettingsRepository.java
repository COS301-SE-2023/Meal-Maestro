package fellowship.mealmaestro.repositories;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.neo4j.driver.Value;
import fellowship.mealmaestro.models.SettingsModel;

@Repository
public class SettingsRepository {

    @Autowired
    private final Driver driver;

    public SettingsRepository(Driver driver){
        this.driver = driver;
    }

   public SettingsModel getSettings(String email){
    try (Session session = driver.session()){
        return session.executeRead(getSettingsTransaction(email));
    }
}

public static TransactionCallback<SettingsModel> getSettingsTransaction(String email) {
    return transaction -> {
        var result = transaction.run("MATCH (User {email: $email})-[:HAS_PREFERENCES]->(s:Settings) " +
                "RETURN s.goal AS goal, s.shoppingInterval AS shoppingInterval, s.foodPreferences AS foodPreferences, " +
                "s.calorieAmount AS calorieAmount, s.budgetRange AS budgetRange, s.macroRatio AS macroRatio, " +
                "s.allergies AS allergies, s.cookingTime AS cookingTime, s.userHeight AS userHeight, s.userWeight AS userWeight, " +
                "s.userBMI AS userBMI, s.BMISet AS BMISet, s.cookingTimeSet AS cookingTimeSet, s.allergiesSet AS allergiesSet, " +
                "s.macroSet AS macroSet, s.budgetSet AS budgetSet, s.calorieSet AS calorieSet, s.foodPreferenceSet AS foodPreferenceSet, " +
                "s.shoppingIntervalSet AS shoppingIntervalSet",
                Values.parameters("email", email));

        if (result.hasNext()) {
            var record = result.next();
            SettingsModel settings = new SettingsModel(
                record.get("goal").asString(),
                record.get("shoppingInterval").asString(),
                record.get("foodPreferences").asList(Value::asString),
                record.get("calorieAmount").asInt(),
                record.get("budgetRange").asString(),
                record.get("macroRatio").asList(Value::asNumber), //need to convert to map somehow
                record.get("allergies").asList(Value::asString),
                record.get("cookingTime").asInt(),
                record.get("userHeight").asInt(),
                record.get("userWeight").asInt(),
                record.get("userBMI").asInt(),
                record.get("BMISet").asBoolean(),
                record.get("cookingTimeSet").asBoolean(),
                record.get("allergiesSet").asBoolean(),
                record.get("macroSet").asBoolean(),
                record.get("budgetSet").asBoolean(),
                record.get("calorieSet").asBoolean(),
                record.get("foodPreferenceSet").asBoolean(),
                record.get("shoppingIntervalSet").asBoolean()
            );
            return settings;
        }
        return null;
    };
}

public void updateSettings(SettingsModel request, String email) {
    
}

}


