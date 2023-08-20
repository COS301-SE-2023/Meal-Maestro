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

    public String ALL_SETTINGS;

    public SettingsModel getSettings(String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        SettingsModel settings = user.getSettings();

        ALL_SETTINGS = makeString(settings);

        return settings;
    }

    public void updateSettings(SettingsModel request, String token) {
        this.makeString(request);
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

    public String makeString(SettingsModel request) {

        String s = "";

        if (request == null) {
            return "No settings to use";
        } else {
            if (request.getGoal() != null && !request.getGoal().isEmpty()) {
                s += "The goal: " + request.getGoal().toString() + ". ";
            }
            if (request.getBudgetRange() != null && !request.getBudgetRange().isEmpty()) {
                s += "The budget range is: " + request.getBudgetRange().toString() + ". ";
            }
            if (request.getCalorieAmount() != 0) {
                s += "The average daily calorie goal is: " + request.getCalorieAmount() + ". ";
            }
            if (request.getCookingTime() != null && !request.getCookingTime().isEmpty()) {
                s += "The average cooking time per meal is : " + request.getCookingTime().toString() + ". ";
            }
            if (request.getShoppingInterval() != null && !request.getShoppingInterval().isEmpty()) {
                s += "The grocery shopping interval is: " + request.getShoppingInterval().toString() + ". ";
            }
            if (request.getUserBMI() != 0) {
                s += "The user's BMI is: " + request.getUserBMI() + ". ";
            }
            if (request.getFoodPreferences() != null && !request.getFoodPreferences().isEmpty()) {
                String slyn = request.getFoodPreferences().toString();

                slyn = slyn.substring(1, slyn.length() - 1);

                s += "The user eats like " + slyn + ". ";
            }
            if (request.getAllergies() != null && !request.getAllergies().isEmpty()) {
                s += "The user's allergens: " + request.getAllergies().toString() + ". ";
            }

            int protein = request.getProtein();
            int carbs = request.getCarbs();
            int fat = request.getFat();
            if (protein != 0 && carbs != 0 && fat != 0) {
                s += "The user's macro ratio is: " + protein + " protein, " + carbs + " carbs, " + fat + " fat. ";
            }

            // System.out.println("HERE IS = " + s);

            return s;

        }

    }

}