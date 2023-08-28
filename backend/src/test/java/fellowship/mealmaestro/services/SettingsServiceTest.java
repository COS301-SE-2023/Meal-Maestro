package fellowship.mealmaestro.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import fellowship.mealmaestro.models.SettingsModel;
import fellowship.mealmaestro.repositories.SettingsRepository;
import fellowship.mealmaestro.services.auth.JwtService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest

public class SettingsServiceTest {

    @InjectMocks
    SettingsService settingsService;

    @Mock
    JwtService jwtService;

    @Mock
    SettingsRepository settingsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSettings() {
        SettingsModel settingsModel = new SettingsModel();
        settingsModel.setGoal("Lose Weight");
        settingsModel.setShoppingInterval("Weekly");
        settingsModel.setFoodPreferences(Arrays.asList("Vegetarian"));
        settingsModel.setCalorieAmount(2000);
        settingsModel.setBudgetRange("Low");
        Map<String, Integer> macroRatio = new HashMap<>();
        macroRatio.put("protein", 40);
        macroRatio.put("carbs", 40);
        macroRatio.put("fat", 20);
        settingsModel.setMacroRatio(macroRatio);
        settingsModel.setAllergies(Arrays.asList("Peanuts"));
        settingsModel.setCookingTime("30 minutes");
        settingsModel.setUserHeight(180);
        settingsModel.setUserWeight(70);
        settingsModel.setUserBMI(22);
        
        when(jwtService.extractUserEmail("validToken")).thenReturn("test@example.com");
        when(settingsRepository.getSettings("test@example.com")).thenReturn(settingsModel);
        
        SettingsModel result = settingsService.getSettings("validToken");

        assertEquals(settingsModel, result);
    }

    @Test
    public void testUpdateSettings() {
        SettingsModel settingsModel = new SettingsModel();
        settingsModel.setGoal("Gain Weight");
        settingsModel.setShoppingInterval("Monthly");
        settingsModel.setFoodPreferences(Arrays.asList("Vegan"));
        settingsModel.setCalorieAmount(3000);
        settingsModel.setBudgetRange("High");
        Map<String, Integer> macroRatio = new HashMap<>();
        macroRatio.put("protein", 30);
        macroRatio.put("carbs", 50);
        macroRatio.put("fat", 20);
        settingsModel.setMacroRatio(macroRatio);
        settingsModel.setAllergies(Arrays.asList("Dairy"));
        settingsModel.setCookingTime("45 minutes");
        settingsModel.setUserHeight(175);
        settingsModel.setUserWeight(75);
        settingsModel.setUserBMI(24);

        when(jwtService.extractUserEmail("validToken")).thenReturn("test@example.com");

        settingsService.updateSettings(settingsModel, "validToken");

        verify(settingsRepository).updateSettings(settingsModel, "test@example.com");
    }
}
