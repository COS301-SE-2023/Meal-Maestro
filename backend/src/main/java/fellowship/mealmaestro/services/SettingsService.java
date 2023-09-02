package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.SettingsModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.repositories.SettingsRepository;
import fellowship.mealmaestro.repositories.UserRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class SettingsService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SettingsRepository SettingsRepository;

    @Autowired
    private UserRepository userRepository;

    public SettingsModel getSettings(String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        SettingsModel settings = user.getSettings();

        return settings;
    }

    public void updateSettings(SettingsModel request, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        SettingsModel settings = user.getSettings();

        settings.setGoal(request.getGoal());
        settings.setBudgetRange(request.getBudgetRange());
        settings.setCalorieAmount(request.getCalorieAmount());
        settings.setCookingTime(request.getCookingTime());
        settings.setShoppingInterval(request.getShoppingInterval());
        settings.setUserHeight(request.getUserHeight());
        settings.setUserWeight(request.getUserWeight());
        settings.setUserBMI(request.getUserBMI());
        settings.setFoodPreferences(request.getFoodPreferences());
        settings.setAllergies(request.getAllergies());
        settings.setProtein(request.getProtein());
        settings.setCarbs(request.getCarbs());
        settings.setFat(request.getFat());

        settings.setShoppingIntervalSet(request.getShoppingIntervalSet());
        settings.setFoodPreferenceSet(request.getFoodPreferenceSet());
        settings.setCalorieSet(request.getCalorieSet());
        settings.setBudgetSet(request.getBudgetSet());
        settings.setMacroSet(request.getMacroSet());
        settings.setAllergiesSet(request.getAllergiesSet());
        settings.setCookingTimeSet(request.getCookingTimeSet());
        settings.setBmiset(request.getBmiset());

        SettingsRepository.save(settings);
    }
}