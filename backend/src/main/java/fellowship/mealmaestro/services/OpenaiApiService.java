package fellowship.mealmaestro.services;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

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
    private final ObjectMapper jsonMapper;
    private final OpenaiPromptBuilder pBuilder;

    public OpenaiApiService(WebClient.Builder webClientBuilder, ObjectMapper jsonMapper, OpenaiPromptBuilder pBuilder) {
        this.webClient = webClientBuilder.build();
        this.jsonMapper = jsonMapper;
        this.pBuilder = pBuilder;
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

    public String fetchMealResponse(String type, String token) throws JsonMappingException, JsonProcessingException {
        String jsonResponse = getJSONResponse(type, token);
        if (jsonResponse.equals("Timeout")) {
            jsonResponse = getJSONResponse(type, token);
        }
        if (jsonResponse.equals("Error") || jsonResponse.equals("Timeout")) {
            return "{\"error\":\"error\"}";
        }

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

        return text;
    }

    public String getJSONResponse(String Type, String token) throws JsonProcessingException {

        OpenAIChatRequest prompt;
        String jsonRequest;

        prompt = pBuilder.buildPrompt(Type, token);
        jsonRequest = jsonMapper.writeValueAsString(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        System.out.println("Sending request to OpenAI");

        try {
            String response = webClient.post()
                    .uri(OPENAI_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(h -> h.setAll(headers.toSingleValueMap()))
                    .body(Mono.just(jsonRequest), String.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            return response;
        } catch (RuntimeException e) {
            if (e.getCause() instanceof TimeoutException) {
                System.out.println("Timeout");
                return "Timeout";
            } else {
                System.out.println("Error");
                return "Error";
            }
        }
    }

}
