package fellowship.mealmaestro.services;

import org.springframework.stereotype.Service;

@Service
public class OpenaiPromptBuilder {

    public String buildPrompt(String Type){
        String prompt = "";
        
        prompt += buildContext(Type);
        prompt += buildGoal();
        prompt += buildFormat();
        prompt += buildSubtasks();
        prompt += buildExample();

        prompt +="\r\n";
        return prompt;
    }
    
    public String buildContext(String Type){
        String res = "";
        res = "Act as a system that creates a meal for a user.\n"
        +"The meal should be a "+ Type +" and should not be difficult to cook.\n";
        return res;
    }

    public String buildGoal(){
        String res = "";
        res = "Pick a meal based on this information and use the following format, a JSON object:\n";
        return res;
    }

    public String buildFormat(){
        String res = "";
        res = "{\n    \"name\":\"meal name\",\n    \"description\":\"short meal description\",\n    \"cookingTime\":\"meal cooking time\",\n"
        + "\n    \"ingredients\":\"list of ingredients seperated by a new line\",\n    \"instructions\":\"step by step instructions, numbered, and seperated by new lines\"\n}\n";
        return res;
    }

    public String buildSubtasks(){
        String res = "";
        res = "Then add that meals ingredients, and cooking instructions\n";
        return res;
    }

    public String buildExample(){
        String res = "";
        res = "Example:\n{\n    \"name\":\"Spaghetti\",\n    \"description\":\"a classic hearty italian dish of mince tomato and pasta\",\n    \"cookingTime\":\"40 minutes\",\n    \"ingredients\":\"Linguini/r/nMince/r/nTomato Pasta Sauce\",\n    \"instructions\":\"1. Bring a pot of water to a boil and then add a dash of salt and the Pasta/r/n2. Brown the mince in a pan/r/n3. Add the tomato sauce to the mince and set to simmer/r/n4. Safely remove and strain the pasta/r/n5. Turn off the mince and sauce when ready\"\n}";
        return res;
    }
}
