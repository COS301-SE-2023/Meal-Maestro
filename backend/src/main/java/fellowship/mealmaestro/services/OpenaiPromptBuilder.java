package fellowship.mealmaestro.services;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import fellowship.mealmaestro.models.OpenAIChatRequest;
import fellowship.mealmaestro.models.OpenAIChatRequest.Message;
import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;
import fellowship.mealmaestro.services.auth.JwtService;
import jakarta.annotation.PostConstruct;

@Service
public class OpenaiPromptBuilder {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private Random rand;

    public OpenaiPromptBuilder(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        rand = new Random(System.currentTimeMillis());
    }

    public OpenAIChatRequest buildPrompt(String type, String token) throws JsonProcessingException {

        OpenAIChatRequest request = new OpenAIChatRequest();
        request.setModel("gpt-3.5-turbo");

        OpenAIChatRequest.Message systemMessage = buildSystemMessage();
        OpenAIChatRequest.Message userMessage = buildUserMessage(type, token);

        request.setMessages(List.of(systemMessage, userMessage));

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

        double random = rand.nextDouble();

        OpenAIChatRequest.Message userMessage = new OpenAIChatRequest.Message();

        System.out.println("1st random: " + random);

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

        random = rand.nextDouble();
        System.out.println("2nd random: " + random);

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

        random = rand.nextDouble();
        System.out.println("3rd random: " + random);
        userMessage.setRole("user");
        if (random < 0.5) {
            userMessage.setContent("I want to cook a " + type + " meal. " + pantryFoods + ". " + settings);
        } else {
            userMessage.setContent("I want to cook a " + type + " meal. " + settings + ". " + pantryFoods);
        }

        return userMessage;
    }

    public OpenAIChatRequest buildPrompt(String type, String token, String fromIngredients) throws JsonProcessingException {

        OpenAIChatRequest request = new OpenAIChatRequest();
        request.setModel("gpt-3.5-turbo");

        OpenAIChatRequest.Message systemMessage = buildSystemMessage();
        OpenAIChatRequest.Message userMessage = buildUserMessage(type, token, fromIngredients);

        request.setMessages(List.of(systemMessage, userMessage));

        return request;
    }

    public Message buildUserMessage(String type, String token, String fromIngredients) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        String pantryFoods = user.getPantry().toString();
        String settings = user.getSettings().toString();

        double random = rand.nextDouble();

        OpenAIChatRequest.Message userMessage = new OpenAIChatRequest.Message();

        System.out.println("1st random: " + random);

        if (pantryFoods.equals("")) {
            if (random < 0.3) {
                pantryFoods = "I have no food in my pantry";
            } else if (random < 0.6) {
                pantryFoods = "There is no food in my pantry";
            } else {
                pantryFoods = "I haven't got any food in my pantry";
            }
        } else {
            pantryFoods = "I have the following foods in my pantry: " + pantryFoods + " And These are my favourite: " + fromIngredients;
        }

        random = rand.nextDouble();
        System.out.println("2nd random: " + random);

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

        random = rand.nextDouble();
        System.out.println("3rd random: " + random);
        userMessage.setRole("user");
        if (random < 0.5) {
            userMessage.setContent("I want to cook a " + type + " meal. " + pantryFoods + ". " + settings);
        } else {
            userMessage.setContent("I want to cook a " + type + " meal. " + settings + ". " + pantryFoods);
        }

        return userMessage;
    }
}
