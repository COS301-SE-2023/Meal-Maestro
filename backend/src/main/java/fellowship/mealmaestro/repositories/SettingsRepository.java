package fellowship.mealmaestro.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public SettingsRepository(Driver driver) {
        this.driver = driver;
    }

    public SettingsModel getSettings(String email) {
        try (Session session = driver.session()) {
            System.out.println("getSettingscalled");
            return session.executeRead(getSettingsTransaction(email));
        }
    }
   
    public static TransactionCallback<SettingsModel> getSettingsTransaction(String email) {
        System.out.println("getSettingsTransaction");
    
        return transaction -> {
            var result = transaction.run("MATCH (u:User {email: $email})-[:HAS_PREFERENCES]->(p:Preferences) " +
                            "MATCH (p)-[:HAS_INTERVAL]->(i:Interval)" +
                            "MATCH (p)-[:HAS_GOAL]->(g:Goal) " +
                            "MATCH (p)-[:HAS_CALORIE_GOAL]->(c:`Calorie Goal`) " +
                            "MATCH (p)-[:HAS_EATING_STYLE]->(e:`Eating Style`) " +
                            "MATCH (p)-[:HAS_MACRO]->(m:Macro) " +
                            "MATCH (p)-[:HAS_BUDGET]->(b:Budget) " +
                            "MATCH (p)-[:HAS_COOKING_TIME]->(ct:`Cooking Time`) " +
                            "MATCH (p)-[:HAS_ALLERGIES]->(a:Allergies) " +
                            "MATCH (p)-[:HAS_BMI]->(bm:BMI) " +
                            "RETURN i.interval AS shoppingInterval, g.goal AS goal, c.caloriegoal AS calorieAmount, e.style AS foodPreferences, " +
                            "m.protein AS protein, m.carbs AS carbs, m.fat AS fat, " +
                            "b.budgetRange AS budgetRange, ct.value AS cookingTime, a.allergies AS allergies, " +
                            "bm.height AS userHeight, bm.weight AS userWeight, bm.BMI AS userBMI",
                    Values.parameters("email", email));
            if (result.hasNext()) {
                var record = result.next();
    
                List<String> foodPreferences = null;
                if (!record.get("foodPreferences").isNull()) {
                    foodPreferences = record.get("foodPreferences").asList(Value::asString);
                }
    
                List<String> allergies = null;
                if (!record.get("allergies").isNull()) {
                    allergies = record.get("allergies").asList(Value::asString);
                }
    
                Map<String, Integer> macroRatio = new HashMap<>();
                Integer protein = record.get("protein").isNull() ? 0 : record.get("protein").asInt();
                Integer carbs = record.get("carbs").isNull() ? 0 : record.get("carbs").asInt();
                Integer fat = record.get("fat").isNull() ? 0 : record.get("fat").asInt();
                
                macroRatio.put("protein", protein);
                macroRatio.put("carbs", carbs);
                macroRatio.put("fat", fat);


                System.out.println("MacroRatio");
                System.out.println(macroRatio);
                
    
                String goal = record.get("goal").isNull() ? null : record.get("goal").asString();
                String shoppingInterval = record.get("shoppingInterval").isNull() ? null : record.get("shoppingInterval").asString();
                Integer calorieAmount = record.get("calorieAmount").isNull() ? 0 : record.get("calorieAmount").asInt();
                String budgetRange = record.get("budgetRange").isNull() ? null : record.get("budgetRange").asString();
                String cookingTime = record.get("cookingTime").isNull() ? "normal" : record.get("cookingTime").asString();
                Integer userHeight = record.get("userHeight").isNull() ? 0 : record.get("userHeight").asInt();
                Integer userWeight = record.get("userWeight").isNull() ? 0 : record.get("userWeight").asInt();

                Integer userBMI = record.get("userBMI").isNull() ? 0 : record.get("userBMI").asInt();
    
                // Update the following lines to default to false if the value is null
                Boolean BMISet = record.get("BMISet").isNull() ? false : record.get("BMISet").asBoolean();
                Boolean cookingTimeSet = record.get("cookingTimeSet").isNull() ? false : record.get("cookingTimeSet").asBoolean();
                Boolean allergiesSet = record.get("allergiesSet").isNull() ? false : record.get("allergiesSet").asBoolean();
                Boolean macroSet = record.get("macroSet").isNull() ? false : record.get("macroSet").asBoolean();
                Boolean budgetSet = record.get("budgetSet").isNull() ? false : record.get("budgetSet").asBoolean();
                Boolean calorieSet = record.get("calorieSet").isNull() ? false : record.get("calorieSet").asBoolean();
                Boolean foodPreferenceSet = record.get("foodPreferenceSet").isNull() ? false : record.get("foodPreferenceSet").asBoolean();
                Boolean shoppingIntervalSet = record.get("shoppingIntervalSet").isNull() ? false : record.get("shoppingIntervalSet").asBoolean();
    
                SettingsModel settings = new SettingsModel(
                        goal,
                        shoppingInterval,
                        foodPreferences,
                        calorieAmount,  
                        budgetRange,
                        macroRatio,
                        allergies,
                        cookingTime,
                        userHeight,
                        userWeight,
                        userBMI,
                        BMISet,
                        cookingTimeSet,
                        allergiesSet,
                        macroSet,
                        budgetSet,
                        calorieSet,
                        foodPreferenceSet,
                        shoppingIntervalSet
                );
               
                
                return settings;

            }
            return null;
        };
    }

    private static Map<String, Integer> getMacroRatioFromRecord(Record record) {
        Map<String, Object> macroRatioValue = record.get("macroRatio").asMap();
        Map<String, Integer> macroRatioMap = new HashMap<>();
    
        for (Map.Entry<String, Object> entry : macroRatioValue.entrySet()) {
            String key = entry.getKey();
            Integer value = ((Number) entry.getValue()).intValue();
            macroRatioMap.put(key, value);
        }
        System.out.println("MacroRatioMap");
        System.out.println(macroRatioMap);
    
        return macroRatioMap;
    }
    

    

  public void updateSettings(SettingsModel request, String email) {
                        System.out.println("UpdateSettings");
        try (Session session = driver.session()) {
            session.executeWrite(updateSettingsTransaction(request, email));
        }
    }

    public static TransactionCallback<Void> updateSettingsTransaction(SettingsModel request, String email) {
        System.out.println("UpdateSettingsTransaction");
        return transaction -> {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("email", email);
    
            // Settings to update
            parameters.put("goal", request.getGoal());
            parameters.put("shoppingInterval", request.getShoppingInterval());
            parameters.put("foodPreferences", request.getFoodPreferences());
            parameters.put("calorieAmount", request.getCalorieAmount());
            parameters.put("budgetRange", request.getBudgetRange());
    
            // Split the macroRatio into individual elements
            Map<String, Integer> macroRatio = request.getMacroRatio();
            parameters.put("protein", macroRatio.get("protein"));
            parameters.put("carbs", macroRatio.get("carbs"));
            parameters.put("fat", macroRatio.get("fat"));
    
            parameters.put("allergies", request.getAllergies());
            parameters.put("cookingTime", request.getCookingTime());
            parameters.put("userHeight", request.getUserHeight());
            parameters.put("userWeight", request.getUserWeight());
            parameters.put("userBMI", request.getUserBMI());
            System.out.println(parameters);
    
            String cypherQuery = "MATCH (u:User {email: $email})-[:HAS_PREFERENCES]->(p:Preferences) " +
                                "MATCH (p)-[:HAS_INTERVAL]->(i:Interval)" +
                                "MATCH (p)-[:HAS_GOAL]->(g:Goal) " +
                                "MATCH (p)-[:HAS_CALORIE_GOAL]->(c:`Calorie Goal`) " +
                                "MATCH (p)-[:HAS_EATING_STYLE]->(e:`Eating Style`) " +
                                "MATCH (p)-[:HAS_MACRO]->(m:Macro) " +
                                "MATCH (p)-[:HAS_BUDGET]->(b:Budget) " +
                                "MATCH (p)-[:HAS_BMI]->(bm:BMI) " +
                                "MATCH (p)-[:HAS_COOKING_TIME]->(ct:`Cooking Time`) " +
                                "MATCH (p)-[:HAS_ALLERGIES]->(a:Allergies) " +
                                "SET i.interval = $shoppingInterval, " +
                                "g.goal = $goal," +
                                "c.caloriegoal = $calorieAmount," +
                                "e.style = $foodPreferences," +
                                "m.protein = $protein," +
                                "m.carbs = $carbs," +
                                "m.fat = $fat," +
                                "b.budgetRange = $budgetRange," +
                                "bm.height = $userHeight," +
                                "bm.weight = $userWeight," +
                                "bm.BMI = $userBMI," +
                                "ct.value = $cookingTime," +
                                "a.allergies = $allergies";
    
            transaction.run(cypherQuery, parameters);
            return null;
        };
    }
    
    
}