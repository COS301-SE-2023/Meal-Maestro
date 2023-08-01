package fellowship.mealmaestro.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest

public class SettingsModelTest {

    SettingsModel settingsModel;

    @BeforeEach
    public void setup() {
        settingsModel = new SettingsModel();
    }

    @Test
    public void testSetGoal() {
        settingsModel.setGoal("Lose Weight");
        assertEquals("Lose Weight", settingsModel.getGoal());
    }

    @Test
    public void testSetShoppingInterval() {
        settingsModel.setShoppingInterval("Weekly");
        assertEquals("Weekly", settingsModel.getShoppingInterval());
    }

    @Test
    public void testSetFoodPreferences() {
        settingsModel.setFoodPreferences(Arrays.asList("Vegetarian"));
        assertEquals(Arrays.asList("Vegetarian"), settingsModel.getFoodPreferences());
    }

    @Test
    public void testSetCalorieAmount() {
        settingsModel.setCalorieAmount(2000);
        assertEquals(2000, settingsModel.getCalorieAmount());
    }

    @Test
    public void testSetBudgetRange() {
        settingsModel.setBudgetRange("Low");
        assertEquals("Low", settingsModel.getBudgetRange());
    }

    @Test
    public void testSetMacroRatio() {
        Map<String, Integer> macroRatio = new HashMap<>();
        macroRatio.put("protein", 40);
        macroRatio.put("carbs", 40);
        macroRatio.put("fat", 20);
        settingsModel.setMacroRatio(macroRatio);
        assertEquals(macroRatio, settingsModel.getMacroRatio());
    }

    @Test
    public void testSetAllergies() {
        settingsModel.setAllergies(Arrays.asList("Peanuts"));
        assertEquals(Arrays.asList("Peanuts"), settingsModel.getAllergies());
    }

    @Test
    public void testSetCookingTime() {
        settingsModel.setCookingTime("30 minutes");
        assertEquals("30 minutes", settingsModel.getCookingTime());
    }

    @Test
    public void testSetUserHeight() {
        settingsModel.setUserHeight(180);
        assertEquals(180, settingsModel.getUserHeight());
    }

    @Test
    public void testSetUserWeight() {
        settingsModel.setUserWeight(70);
        assertEquals(70, settingsModel.getUserWeight());
    }

    @Test
    public void testSetUserBMI() {
        settingsModel.setUserBMI(22);
        assertEquals(22, settingsModel.getUserBMI());
    }
}
