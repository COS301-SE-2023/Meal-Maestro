package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import fellowship.mealmaestro.models.SettingsModel;
import fellowship.mealmaestro.repositories.SettingsRepository;
import fellowship.mealmaestro.services.auth.JwtService;


@Service
public class SettingsService {


    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private SettingsRepository SettingsRepository;

    public SettingsModel getSettings(String token){
        String email = jwtService.extractUserEmail(token);
        return SettingsRepository.getSettings(email);
    }

    public void updateSettings(SettingsModel request, String token){
        this.makeString(request);
        String email = jwtService.extractUserEmail(token);
        SettingsRepository.updateSettings(request, email);
    }

    public void makeString(SettingsModel request){
        

    String ALL_SETTINGS = "The goal: " + request.getGoal() +". The budget range is: "+ request.getBudgetRange() +". The average daily calorie goal is: "+ request.getCalorieAmount() +". The average cooking time per meal is : "+ request.getCookingTime() + ". The grocery shopping interval is: "+request.getShoppingInterval() +". The user's BMI is: "+ request.getUserBMI() +". The user eats like "+ request.getFoodPreferences() +". The user's allergens: "+ request.getAllergies() +". The macro ratio for the user is "+ request.getMacroRatio() ;
        
    System.out.println("okokokkokokkookokok");
    System.out.println(ALL_SETTINGS);
        
    }

    
}