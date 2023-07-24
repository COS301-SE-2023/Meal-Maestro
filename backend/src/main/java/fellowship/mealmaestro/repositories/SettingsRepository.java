package fellowship.mealmaestro.repositories;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.neo4j.driver.Values;
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
        String cypherQuery = "MATCH (User {email: $email})-[:HAS_PREFERENCES]->(s:Preferences) ";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", email);

        if (request.isBMISet()) {
            cypherQuery += "SET s.BMISet = $BMISet ";
            parameters.put("BMISet", request.isBMISet());
        }

        if (request.isCookingTimeSet()) {
            cypherQuery += "SET s.cookingTimeSet = $cookingTimeSet ";
            parameters.put("cookingTimeSet", request.isCookingTimeSet());
        }

        if (request.isAllergiesSet()) {
            cypherQuery += "SET s.allergiesSet = $allergiesSet ";
            parameters.put("allergiesSet", request.isAllergiesSet());
        }

        if (request.isMacroSet()) {
            cypherQuery += "SET s.macroSet = $macroSet ";
            parameters.put("macroSet", request.isMacroSet());
        }

        if (request.isBudgetSet()) {
            cypherQuery += "SET s.budgetSet = $budgetSet ";
            parameters.put("budgetSet", request.isBudgetSet());
        }

        if (request.isCalorieSet()) {
            cypherQuery += "SET s.calorieSet = $calorieSet ";
            parameters.put("calorieSet", request.isCalorieSet());
        }

        if (request.isFoodPreferenceSet()) {
            cypherQuery += "SET s.foodPreferenceSet = $foodPreferenceSet ";
            parameters.put("foodPreferenceSet", request.isFoodPreferenceSet());
        }

        if (request.isShoppingIntervalSet()) {
            cypherQuery += "SET s.shoppingIntervalSet = $shoppingIntervalSet ";
            parameters.put("shoppingIntervalSet", request.isShoppingIntervalSet());
        }


        cypherQuery += "SET s.goal = $goal, s.shoppingInterval = $shoppingInterval, s.foodPreferences = $foodPreferences, " +
                "s.calorieAmount = $calorieAmount, s.budgetRange = $budgetRange, s.macroRatio = $macroRatio, " +
                "s.allergies = $allergies, s.cookingTime = $cookingTime, s.userHeight = $userHeight, s.userWeight = $userWeight, " +
                "s.userBMI = $userBMI";

        parameters.put("goal", request.getGoal());
        parameters.put("shoppingInterval", request.getShoppingInterval());
        parameters.put("foodPreferences", request.getFoodPreferences());
        parameters.put("calorieAmount", request.getCalorieAmount());
        parameters.put("budgetRange", request.getBudgetRange());
        parameters.put("macroRatio", request.getMacroRatio());
        parameters.put("allergies", request.getAllergies());
        parameters.put("cookingTime", request.getCookingTime());
        parameters.put("userHeight", request.getUserHeight());
        parameters.put("userWeight", request.getUserWeight());
        parameters.put("userBMI", request.getUserBMI());

        transaction.run(cypherQuery, Values.parameters(parameters));
        return null;
    };
}



}


