package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class openaiApiService {
    Dotenv dotenv = Dotenv.load();
    private static final String OPENAI_URL = "";
    private final String API_KEY = dotenv.get("OPENAI_API_KEY");
    private final RestTemplate restTemplate = new RestTemplate();

    private String model = "gpt-3.5-turbo-0613";
    private String stop = "";
    private double temperature = 0.3;
    private double topP = 0.9;
    private double freqPenalty;
    private double presencePenalty;
    private int maximumLength = 300;

    public String buildPrompt(String context, String goal, String format, String secondaryTasks, String examples) {
        String Prompt =context + "\r\n\r\n" + goal + "\r\n\r\n" + format + "\r\n\r\n" + secondaryTasks + "\r\n\r\n" + examples;
       
        return Prompt;
    }

}
