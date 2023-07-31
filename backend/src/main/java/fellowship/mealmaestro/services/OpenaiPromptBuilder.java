package fellowship.mealmaestro.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenaiPromptBuilder {

    @Autowired
    private SettingsService settingsService;
    public String buildPrompt(String Type) throws JsonProcessingException {
        String prompt = "";
        String preferenceString =settingsService.ALL_SETTINGS;
        prompt += buildContext(Type, preferenceString);
        prompt += buildGoal();
        prompt += buildFormat();
        prompt += buildSubtasks();
        prompt += buildExample();
        prompt += " ";
        
        return prompt;
    }
    public String buildPrompt(String Type, String extendedPrompt) throws JsonProcessingException {
        String prompt = "";

        prompt += buildContext(Type,extendedPrompt);
        prompt += buildGoal();
        prompt += buildFormat();
        prompt += buildSubtasks();
        prompt += buildExample();
      // prompt += "\r\n";
        
        return prompt;
    }

    public String buildContext(String Type) {
        String res = "";
        res = ("Act as a system that creates a meal for a user.The meal should be a " + Type
                + " and should not be difficult to cook.");
        return res;
    }
    public String buildContext(String Type, String extendedPropmpt) {
        String res = "";
        res = ("Act as a system that creates a meal for a user.The meal should be a " + Type
                + " and should not be difficult to cook." + extendedPropmpt);
        return res;
    }

    public String buildGoal() {
        String res = "";
        res = "Pick a meal based on this information and use the following format, a JSON object:";
        return res;
    }

    public String buildFormat() throws JsonProcessingException {
       
        Map<String, Object> params = new HashMap<>();
        params.put("name", "meal name");
        params.put("description", "short description of meal");
        params.put("cookingTime", "meal cooking time");
        params.put("ingredients", "list of ingredients seperated by a new line");
         params.put("instructions", "step by step instructions, numbered, and seperated by new lines");
        
        return new ObjectMapper().writeValueAsString(params.toString());

        
    }

    public String buildSubtasks() {
        String res = "";
        res = "Then add that meals ingredients, and cooking instructions";
        return res;
    }

    public String buildExample() throws JsonProcessingException {
       
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Spaghetti");
        params.put("description", "a classic hearty italian dish of mince tomato and pasta");
        params.put("cookingTime", "40 minutes");
        params.put("ingredients", "Linguini/r/nMince/r/nTomato Pasta Sauce");
        params.put("instructions", "1. Bring a pot of water to a boil and then add a dash of salt and the Pasta/r/n2. Brown the mince in a pan/r/n3. Add the tomato sauce to the mince and set to simmer/r/n4. Safely remove and strain the pasta/r/n5. Turn off the mince and sauce when ready");
        
        return new ObjectMapper().writeValueAsString(params);
    }
}
