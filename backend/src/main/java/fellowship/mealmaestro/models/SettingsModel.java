package fellowship.mealmaestro.models;

import java.util.List;

import java.util.Map;

import org.springframework.data.neo4j.core.schema.Node;

import lombok.Data;

@Data
@Node("Settings")
public class SettingsModel {

    private String goal;
    private String shoppingInterval;
    private List<String> foodPreferences;
    private int calorieAmount;
    private String budgetRange;
    private Map<String, Integer> macroRatio;

    private List<String> allergies;
    private String cookingTime;
    private int userHeight; // consider moving to account
    private int userWeight; // consider moving to account
    private int userBMI;

    private boolean bmiset = false;
    private boolean cookingTimeSet = false;
    private boolean allergiesSet = false;
    private boolean macroSet = false;
    private boolean budgetSet = false;
    private boolean calorieSet = false;
    private boolean foodPreferenceSet = false;
    private boolean shoppingIntervalSet = false;

    public SettingsModel() {
        // Empty constructor with all booleans set to false by default
    }

    public SettingsModel(String goal, String shoppingInterval, List<String> foodPreferences, int calorieAmount,
            String budgetRange, Map<String, Integer> macroRatio, List<String> allergies, String cookingTime,
            int userHeight, int userWeight, int userBMI, boolean bmiset, boolean cookingTimeSet,
            boolean allergiesSet, boolean macroSet, boolean budgetSet, boolean calorieSet,
            boolean foodPreferenceSet, boolean shoppingIntervalSet) {
        this.goal = goal;
        this.shoppingInterval = shoppingInterval;
        this.foodPreferences = foodPreferences;
        this.calorieAmount = calorieAmount;
        this.budgetRange = budgetRange;
        this.macroRatio = macroRatio;

        this.allergies = allergies;
        this.cookingTime = cookingTime;
        this.userHeight = userHeight;
        this.userWeight = userWeight;
        this.userBMI = userBMI;
        this.bmiset = bmiset;
        this.cookingTimeSet = cookingTimeSet;
        this.allergiesSet = allergiesSet;
        this.macroSet = macroSet;
        this.budgetSet = budgetSet;
        this.calorieSet = calorieSet;
        this.foodPreferenceSet = foodPreferenceSet;
        this.shoppingIntervalSet = shoppingIntervalSet;
    }

    public int getUserBMI() {
        return userBMI;
    }

    public void setUserBMI(int userHeight, int userWeight) {
        if (userWeight == 0) {
            this.userBMI = 0;
        } else {
            this.userBMI = userHeight / userWeight;
        }
    }

    public void setUserBMI(int userBMI) {
        this.userBMI = userBMI;
    }
}
