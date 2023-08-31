package fellowship.mealmaestro.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fellowship.mealmaestro.models.OpenAIChatRequest;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.models.OpenAIChatRequest.Message;
import fellowship.mealmaestro.repositories.UserRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class OpenaiPromptBuilder {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    public OpenAIChatRequest buildPrompt(String type, String token) throws JsonProcessingException {
        String prompt = "";
        // String preferenceString = settingsService.ALL_SETTINGS;
        // prompt += buildContext(type, preferenceString);
        // prompt += buildGoal();
        // prompt += buildFormat();
        // prompt += buildSubtasks();
        // prompt += buildExample();
        // prompt += " ";

        OpenAIChatRequest request = new OpenAIChatRequest();
        request.setModel("gpt-3.5-turbo");

        OpenAIChatRequest.Message systemMessage = buildSystemMessage();
        OpenAIChatRequest.Message userMessage = buildUserMessage(type, token);

        request.setMessages(List.of(systemMessage, userMessage));

        System.out.println("HERE IS THE PROMPT: ");
        System.out.println(prompt);

        return request;
    }

    // public String buildPrompt(String type, String extendedPrompt) throws
    // JsonProcessingException {
    // String prompt = "";

    // prompt += buildContext(type, extendedPrompt);
    // prompt += buildGoal();
    // prompt += buildFormat();
    // prompt += buildSubtasks();
    // prompt += buildExample();
    // // prompt += "\r\n";

    // return prompt;
    // }

    public String buildContext(String type) {
        String res = "";
        res = ("Act as a system that creates a meal for a user.The meal should be a " + type
                + " and should not be difficult to cook.");
        return res;
    }

    public String buildContext(String type, String extendedPropmpt) {
        String res = "";
        res = ("Act as a system that creates a meal for a user. The meal should be a " + type
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
        params.put("instructions", "step by step instructions, numbered, and separated by new lines");

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
        params.put("instructions",
                "1. Bring a pot of water to a boil and then add a dash of salt and the Pasta/r/n2. Brown the mince in a pan/r/n3. Add the tomato sauce to the mince and set to simmer/r/n4. Safely remove and strain the pasta/r/n5. Turn off the mince and sauce when ready");

        return new ObjectMapper().writeValueAsString(params);
    }

    public Message buildSystemMessage() {
        OpenAIChatRequest.Message systemMessage = new OpenAIChatRequest.Message();
        systemMessage.setRole("system");
        systemMessage.setContent(
                "You will be given some information about me. You must first use this information to create a meal for me. Then you will return the meal as a JSON object in the following format. {\"name\":\"meal name\",\"description\":\"short description\",\"cookingTime\":\"time to cook\",\"ingredients\":\"list of comma separated ingredients\",\"instructions\":\"numbered step by step instructions separated by new lines\"} Please only return the JSON object");
        return systemMessage;
    }

    public Message buildUserMessage(String type, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        String pantryFoods = user.getPantry().toString();
        String settings = user.getSettings().toString();

        // random with time as seed
        Random rand = new Random(System.currentTimeMillis());
        double random = rand.nextDouble();

        OpenAIChatRequest.Message userMessage = new OpenAIChatRequest.Message();

        if (pantryFoods.equals("")) {
            if (random < 0.3) {
                pantryFoods = "I have no food in my pantry";
            } else if (random < 0.6) {
                pantryFoods = "There is no food in my pantry";
            } else {
                pantryFoods = "I haven't got any food in my pantry";
            }
        } else {
            pantryFoods = "I have the following foods in my pantry: " + pantryFoods;
        }

        if (settings.equals("")) {
            if (random < 0.3) {
                settings = "You can make whatever unique meal you want.";
            } else if (random < 0.6) {
                settings = "You can make whatever meal you want.";
            } else {
                settings = "You can make whatever meal you want, as long as it is " + type + ".";
            }
        } else {
            settings = "Some other useful information about me: " + settings + ".";
        }

        userMessage.setRole("user");
        userMessage.setContent("I want to cook a " + type + " meal. " + pantryFoods + ". " + settings);

        return userMessage;
    }
}
