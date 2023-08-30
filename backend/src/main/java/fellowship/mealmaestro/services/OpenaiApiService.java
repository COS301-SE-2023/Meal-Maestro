package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fellowship.mealmaestro.models.OpenAIChatRequest;
import io.github.cdimascio.dotenv.Dotenv;
import reactor.core.publisher.Mono;

@Service
public class OpenaiApiService {
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    private final static String API_KEY;

    private final WebClient webClient;

    public OpenaiApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    static {
        String apiKey;
        Dotenv dotenv;
        if (System.getenv("OPENAI_API_KEY") != null) {
            apiKey = System.getenv("OPENAI_API_KEY");
        } else {
            try {
                dotenv = Dotenv.load();
                apiKey = dotenv.get("OPENAI_API_KEY");
            } catch (Exception e) {
                dotenv = Dotenv.configure()
                        .ignoreIfMissing()
                        .load();
                apiKey = "No API Key Found";
            }
        }
        API_KEY = apiKey;
    }

    @Autowired
    private ObjectMapper jsonMapper = new ObjectMapper();
    @Autowired
    private OpenaiPromptBuilder pBuilder = new OpenaiPromptBuilder();

    public String fetchMealResponse(String type, String token) throws JsonMappingException, JsonProcessingException {
        System.out.println("Fetching meal response");
        String jsonResponse = getJSONResponse(type, token);
        JsonNode jsonNode = jsonMapper.readTree(jsonResponse);

        JsonNode contentNode = jsonNode
                .path("choices")
                .get(0)
                .path("message")
                .path("content");

        String text = contentNode.asText();

        text = text.replace("\\\"", "\"");
        text = text.replace("\n", "");
        text = text.replace("/r/n", "\\r\\n");
        int index = text.indexOf('{');
        int lastIndex = text.lastIndexOf('}') + 1;
        if (index != -1 && lastIndex != -1 && index < lastIndex) {
            text = text.substring(index, lastIndex);
        }

        System.out.println("HERE IS THE RESPONSE: ");
        System.out.println(text);
        return text;

        // return "{\"instructions\":\"1. Preheat oven to 375 degrees/r/n2. Grease a
        // baking dish with butter/r/n3. Beat together the eggs, milk, and a pinch of
        // salt/r/n4. Place the bread slices in the baking dish and pour the egg mixture
        // over them/r/n5. Bake in the preheated oven for 25 minutes/r/n6. Serve warm
        // with your favorite toppings\",\"name\":\"Baked French
        // Toast\",\"description\":\"a delicious breakfast dish of egg-soaked
        // bread\",\"ingredients\":\"6 slices of bread/r/n3 eggs/r/n3/4 cup of
        // milk/r/nSalt/r/nButter\",\"cookingTime\":\"30 minutes\"}";
    }

    public String getJSONResponse(String Type, String token) throws JsonProcessingException {

        OpenAIChatRequest prompt;
        String jsonRequest;

        prompt = pBuilder.buildPrompt(Type, token);
        jsonRequest = jsonMapper.writeValueAsString(prompt);
        System.out.println(jsonRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        System.out.println("Sending request to OpenAI");

        String response = webClient.post()
                .uri(OPENAI_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setAll(headers.toSingleValueMap()))
                .body(Mono.just(jsonRequest), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }

}
