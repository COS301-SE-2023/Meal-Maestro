package fellowship.mealmaestro.repositories;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.neo4j.driver.Values;
import org.neo4j.driver.internal.value.MapValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.neo4j.driver.Value;
import fellowship.mealmaestro.models.SettingsModel;
import org.neo4j.driver.Record;



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
                getMacroRatioFromRecord(record),
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

//helper function for getSettingsTransaction

private static Map<String, Integer> getMacroRatioFromRecord(Record record) {
    Map<String, Object> macroRatioValue = record.get("macroRatio").asMap();
    Map<String, Integer> macroRatioMap = new HashMap<>();

    for (Map.Entry<String, Object> entry : macroRatioValue.entrySet()) {
        String key = entry.getKey();
        Integer value = ((Number) entry.getValue()).intValue();
        macroRatioMap.put(key, value);
    }

    return macroRatioMap;
}





public void updateSettings(SettingsModel request, String email) {
    try (Session session = driver.session()) {
        session.executeWrite(updateSettingsTransaction(request, email));
    }
}

public static TransactionCallback<Void> updateSettingsTransaction(SettingsModel request, String email) {
    return transaction -> {
        transaction.run("MATCH (User {email: $email})-[:HAS_PREFERENCES]->(s:Settings) " +
                "SET s.goal = $goal, s.shoppingInterval = $shoppingInterval, s.foodPreferences = $foodPreferences, " +
                "s.calorieAmount = $calorieAmount, s.budgetRange = $budgetRange, s.macroRatio = $macroRatio, " +
                "s.allergies = $allergies, s.cookingTime = $cookingTime, s.userHeight = $userHeight, s.userWeight = $userWeight, " +
                "s.userBMI = $userBMI, s.BMISet = $BMISet, s.cookingTimeSet = $cookingTimeSet, s.allergiesSet = $allergiesSet, " +
                "s.macroSet = $macroSet, s.budgetSet = $budgetSet, s.calorieSet = $calorieSet, s.foodPreferenceSet = $foodPreferenceSet, " +
                "s.shoppingIntervalSet = $shoppingIntervalSet",
                Values.parameters("email", email,
                        "goal", request.getGoal(),
                        "shoppingInterval", request.getShoppingInterval(),
                        "foodPreferences", request.getFoodPreferences(),
                        "calorieAmount", request.getCalorieAmount(),
                        "budgetRange", request.getBudgetRange(),
                        "macroRatio", request.getMacroRatio(),
                        "allergies", request.getAllergies(),
                        "cookingTime", request.getCookingTime(),
                        "userHeight", request.getUserHeight(),
                        "userWeight", request.getUserWeight(),
                        "userBMI", request.getUserBMI(),
                        "BMISet", request.isBMISet(),
                        "cookingTimeSet", request.isCookingTimeSet(),
                        "allergiesSet", request.isAllergiesSet(),
                        "macroSet", request.isMacroSet(),
                        "budgetSet", request.isBudgetSet(),
                        "calorieSet", request.isCalorieSet(),
                        "foodPreferenceSet", request.isFoodPreferenceSet(),
                        "shoppingIntervalSet", request.isShoppingIntervalSet()
                ));
        return null;
    };
}



}


