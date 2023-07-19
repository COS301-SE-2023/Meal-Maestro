package fellowship.mealmaestro.models;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import fellowship.mealmaestro.models.auth.AuthorityRoleModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public class SettingsModel{

    private String goal; 
    private String shoppingInterval;
    private List<String> foodPreferences;
    private int calorieAmount;
    private String budgetRange;
    private Map<String, Integer> macroRatio;
    private List<String> allergies;
    private int cookingTime;
    private int userHeight; // consider moving to account
    private int userWeight; // consider moving to account
    private int userBMI;

    private boolean BMISet;
    private boolean cookingTimeSet;
    private boolean allergiesSet;
    private boolean macroSet;
    private boolean budgetSet;
    private boolean calorieSet;
    private boolean foodPreferenceSet;
    private boolean shoppingIntervalSet;

    public SettingsModel(String goal, String shoppingInterval, List<String> foodPreferences, int calorieAmount,
    String budgetRange, Map<String, Integer> macroRatio, List<String> allergies, int cookingTime,
    int userHeight, int userWeight, int userBMI, boolean BMISet, boolean cookingTimeSet,
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
this.BMISet = BMISet;
this.cookingTimeSet = cookingTimeSet;
this.allergiesSet = allergiesSet;
this.macroSet = macroSet;
this.budgetSet = budgetSet;
this.calorieSet = calorieSet;
this.foodPreferenceSet = foodPreferenceSet;
this.shoppingIntervalSet = shoppingIntervalSet;
}

 

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getShoppingInterval() {
        return shoppingInterval;
    }

    public void setShoppingInterval(String shoppingInterval) {
        this.shoppingInterval = shoppingInterval;
    }

    public List<String> getFoodPreferences() {
        return foodPreferences;
    }

    public void setFoodPreferences(List<String> foodPreferences) {
        this.foodPreferences = foodPreferences;
    }

    public int getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(int calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public String getBudgetRange() {
        return budgetRange;
    }

    public void setBudgetRange(String budgetRange) {
        this.budgetRange = budgetRange;
    }

    public Map<String, Integer> getMacroRatio() {
        return macroRatio;
    }

    public void setMacroRatio(Map<String, Integer> macroRatio) {
        this.macroRatio = macroRatio;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public int getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(int userHeight) {
        this.userHeight = userHeight;
    }

    public int getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(int userWeight) {
        this.userWeight = userWeight;
    }

    public int getUserBMI() {
        return userBMI;
    }

    public void setUserBMI(int userBMI) {
        this.userBMI = userBMI;
    }

    public boolean isBMISet() {
        return BMISet;
    }

    public void setBMISet(boolean BMISet) {
        this.BMISet = BMISet;
    }

    public boolean isCookingTimeSet() {
        return cookingTimeSet;
    }

    public void setCookingTimeSet(boolean cookingTimeSet) {
        this.cookingTimeSet = cookingTimeSet;
    }

    public boolean isAllergiesSet() {
        return allergiesSet;
    }

    public void setAllergiesSet(boolean allergiesSet) {
        this.allergiesSet = allergiesSet;
    }

    public boolean isMacroSet() {
        return macroSet;
    }

    public void setMacroSet(boolean macroSet) {
        this.macroSet = macroSet;
    }

    public boolean isBudgetSet() {
        return budgetSet;
    }

    public void setBudgetSet(boolean budgetSet) {
        this.budgetSet = budgetSet;
    }

    public boolean isCalorieSet() {
        return calorieSet;
    }

    public void setCalorieSet(boolean calorieSet) {
        this.calorieSet = calorieSet;
    }

    public boolean isFoodPreferenceSet() {
        return foodPreferenceSet;
    }

    public void setFoodPreferenceSet(boolean foodPreferenceSet) {
        this.foodPreferenceSet = foodPreferenceSet;
    }

    public boolean isShoppingIntervalSet() {
        return shoppingIntervalSet;
    }

    public void setShoppingIntervalSet(boolean shoppingIntervalSet) {
        this.shoppingIntervalSet = shoppingIntervalSet;
    }
   

}
