package fellowship.mealmaestro.models;

import java.util.List;

import java.util.UUID;

import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.Data;

@Data
@Node("Settings")
public class SettingsModel {

    @Id
    private UUID id;

    @Version
    private Long version;

    private String goal;
    private String shoppingInterval;
    private List<String> foodPreferences;
    private int calorieAmount;
    private String budgetRange;
    private int protein;
    private int carbs;
    private int fat;

    private List<String> allergies;
    private String cookingTime;
    private double userHeight; // consider moving to account
    private double userWeight; // consider moving to account
    private double userBMI;

    private boolean bmiset;
    private boolean cookingTimeSet;
    private boolean allergiesSet;
    private boolean macroSet;
    private boolean budgetSet;
    private boolean calorieSet;
    private boolean foodPreferenceSet;
    private boolean shoppingIntervalSet;

    public SettingsModel() {
        // Empty constructor with all booleans set to false by default
    }

    public SettingsModel(String goal, String shoppingInterval, List<String> foodPreferences, int calorieAmount,
            String budgetRange, int protein, int carbs, int fat, List<String> allergies, String cookingTime,
            double userHeight, double userWeight, double userBMI, boolean bmiset, boolean cookingTimeSet,
            boolean allergiesSet, boolean macroSet, boolean budgetSet, boolean calorieSet,
            boolean foodPreferenceSet, boolean shoppingIntervalSet, UUID id) {
        this.goal = goal;
        this.shoppingInterval = shoppingInterval;
        this.foodPreferences = foodPreferences;
        this.calorieAmount = calorieAmount;
        this.budgetRange = budgetRange;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;

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
        this.id = id;
    }

    public double getUserBMI() {
        return userBMI;
    }

    public void setUserBMI(double userHeight, double userWeight) {
        if (userWeight == 0) {
            this.userBMI = 0;
        } else {
            double heightInMeters = userHeight / 100.0;
            // set userBMI to 2 decimal places
            this.userBMI = Math.round((userWeight / (heightInMeters * heightInMeters)) * 100.0) / 100.0;

        }
    }

    public void setUserBMI(double userBMI) {
        this.userBMI = userBMI;
    }

    public void setAllBoolean() {
        this.bmiset = false;
        this.cookingTimeSet = false;
        this.allergiesSet = false;
        this.macroSet = false;
        this.budgetSet = false;
        this.calorieSet = false;
        this.foodPreferenceSet = false;
        this.shoppingIntervalSet = false;
    }

    public void setShoppingIntervalSet(boolean shoppingIntervalSet) {
        this.shoppingIntervalSet = shoppingIntervalSet;
    }

    public void setFoodPreferenceSet(boolean foodPreferenceSet) {
        this.foodPreferenceSet = foodPreferenceSet;
    }

    public void setCalorieSet(boolean calorieSet) {
        this.calorieSet = calorieSet;
    }

    public void setBudgetSet(boolean budgetSet) {
        this.budgetSet = budgetSet;
    }

    public void setMacroSet(boolean macroSet) {
        this.macroSet = macroSet;
    }

    public void setAllergiesSet(boolean allergiesSet) {
        this.allergiesSet = allergiesSet;
    }

    public void setCookingTimeSet(boolean cookingTimeSet) {
        this.cookingTimeSet = cookingTimeSet;
    }

    public void setBmiset(boolean bmiset) {
        this.bmiset = bmiset;
    }

    public boolean getShoppingIntervalSet() {
        return shoppingIntervalSet;
    }

    public boolean getFoodPreferenceSet() {
        return foodPreferenceSet;
    }

    public boolean getCalorieSet() {
        return calorieSet;
    }

    public boolean getBudgetSet() {
        return budgetSet;
    }

    public boolean getMacroSet() {
        return macroSet;
    }

    public boolean getAllergiesSet() {
        return allergiesSet;
    }

    public boolean getCookingTimeSet() {
        return cookingTimeSet;
    }

    public boolean getBmiset() {
        return bmiset;
    }
}
