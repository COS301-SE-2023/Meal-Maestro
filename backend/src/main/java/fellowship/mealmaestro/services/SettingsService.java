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

    public String ALL_SETTINGS;

    public SettingsModel getSettings(String token){
        String email = jwtService.extractUserEmail(token);
        SettingsModel settingsModel = SettingsRepository.getSettings(email);
        ALL_SETTINGS = makeString(settingsModel);
        return settingsModel;
    }

    public void updateSettings(SettingsModel request, String token){
        this.makeString(request);
        String email = jwtService.extractUserEmail(token);
        SettingsRepository.updateSettings(request, email);
        ALL_SETTINGS = makeString(request);
    }

    public String makeString(SettingsModel request){

        String s = "";

        if (request == null){
            return "No settings to use";
        }
        else 
        {
            if (request.getGoal() != null && !request.getGoal().isEmpty()){
                s += "The goal: " + request.getGoal().toString() + ". ";
            }
            if (request.getBudgetRange() != null && !request.getBudgetRange().isEmpty()){
                s += "The budget range is: "+ request.getBudgetRange().toString() + ". ";
            }
            if (request.getCalorieAmount() != 0 ){
                s += "The average daily calorie goal is: "+ request.getCalorieAmount() + ". ";
            }
            if (request.getCookingTime() != null && !request.getCookingTime().isEmpty()){
                s += "The average cooking time per meal is : "+ request.getCookingTime().toString() + ". ";
            }
            if (request.getShoppingInterval() != null && !request.getShoppingInterval().isEmpty()){
                s += "The grocery shopping interval is: "+request.getShoppingInterval().toString() + ". ";
            }
            if (request.getUserBMI() != 0){
                s += "The user's BMI is: "+ request.getUserBMI() + ". ";
            }
            if (request.getFoodPreferences() != null && !request.getFoodPreferences().isEmpty()){
                String slyn = request.getFoodPreferences().toString();
                
                slyn = slyn.substring(1, slyn.length()-1);

                s += "The user eats like "+ slyn + ". ";
            }
            if (request.getAllergies() != null && !request.getAllergies().isEmpty()){
                s += "The user's allergens: "+ request.getAllergies().toString() + ". ";
            }

            String slyn = request.getMacroRatio().toString();
            if (slyn.equals("{protein=0, carbs=0, fat=0}")){
               
            }
            else{
                slyn = slyn.substring(1, slyn.length()-1);
                s += "The macro ratio for the user is "+ slyn + ". ";
            }
        

           
            System.out.println("HERE IS = "+s);

            return s;  

        }

       
        
//     String s ="";// + "The goal: " + request.getGoal().toString() 
// //    +". The budget range is: "+ request.getBudgetRange().toString() 
// //    +". The average daily calorie goal is: "+ request.getCalorieAmount()
// //     +". The average cooking time per meal is : "+ request.getCookingTime().toString() 
// //     + ". The grocery shopping interval is: "+request.getShoppingInterval().toString() 
// //     +". The user's BMI is: "+ request.getUserBMI() 
//     //  + ". The user eats like "+ request.getFoodPreferences().toString() 
//     //   +". The user's allergens: "+ request.getAllergies().toString()
// //      +". The macro ratio for the user is "+ request.getMacroRatio().toString() 
//      //;
//      if(request.isFoodPreferenceSet())
//       {
//         s += ". The user prefers "+ request.getFoodPreferences().toString() ;
//      }
//      if(request.isAllergiesSet())
//      {}else {
//         s += ". The user is allergic to "+ request.getAllergies().toString() ;
//      }
        
//     System.out.println(s);

//     return s;
        
    }

    
}