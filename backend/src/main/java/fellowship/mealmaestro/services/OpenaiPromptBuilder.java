package fellowship.mealmaestro.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

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

        OpenAIChatRequest request = new OpenAIChatRequest();
        request.setModel("gpt-3.5-turbo");

        OpenAIChatRequest.Message systemMessage = buildSystemMessage();
        OpenAIChatRequest.Message userMessage = buildUserMessage(type, token);

        request.setMessages(List.of(systemMessage, userMessage));

        System.out.println("HERE IS THE PROMPT: ");
        System.out.println(userMessage.getContent());

        return request;
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
        if (random < 0.5) {
            userMessage.setContent("I want to cook a " + type + " meal. " + pantryFoods + ". " + settings);
        } else {
            userMessage.setContent("I want to cook a " + type + " meal. " + settings + ". " + pantryFoods);
        }

        return userMessage;
    }
}
