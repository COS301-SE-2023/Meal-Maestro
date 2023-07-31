package fellowship.mealmaestro.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class OpenaiApiService {
    Dotenv dotenv = Dotenv.load();
    private static final String OPENAI_URL = "https://api.openai.com/v1/completions";

    private final static String API_KEY;
    private final RestTemplate restTemplate = new RestTemplate();

    static{
        String apiKey;
        Dotenv dotenv;
        if (System.getenv("OPENAI_API_KEY") != null) {
            apiKey = System.getenv("OPENAI_API_KEY");
        } else {
            try{
                dotenv = Dotenv.load();
                apiKey = dotenv.get("OPENAI_API_KEY");
            } catch (Exception e){
                dotenv = Dotenv.configure()
                                .ignoreIfMissing()
                                .load();
                apiKey = "No API Key Found";
            }
        }
        API_KEY = apiKey;
    }

    private String model = "text-davinci-003";
    private String stop = "";

    private double temperature = 0.5;
    private double topP = 1.0;
    private double freqPenalty = 0.0;
    private double presencePenalty = 0.0;

    private int maximumTokenLength = 800;

    // potential vars

    // will make a few prompts and return best, heavy on token use
    private int bestOfN = 1;
    // detect abuse
    // private String user = "";
    // echo back prompt and its compeletion
    // private boolean echo = false;
    // stream prompt as it generates
    // private boolean stream = false;

    @Autowired
    private ObjectMapper jsonMapper = new ObjectMapper();
    @Autowired
    private OpenaiPromptBuilder pBuilder = new OpenaiPromptBuilder();

    public String fetchMealResponse(String Type) throws JsonMappingException, JsonProcessingException {
        // String jsonResponse = getJSONResponse(Type);
        // JsonNode jsonNode = jsonMapper.readTree(jsonResponse);

        // String text = jsonNode.get("choices").get(0).get("text").asText();
        // text = text.replace("\\\"", "\"");
        // text = text.replace("\n", "");
        // return text;

        return "{\"instructions\":\"1. Preheat oven to 375 degrees/r/n2. Grease a baking dish with butter/r/n3. Beat together the eggs, milk, and a pinch of salt/r/n4. Place the bread slices in the baking dish and pour the egg mixture over them/r/n5. Bake in the preheated oven for 25 minutes/r/n6. Serve warm with your favorite toppings\",\"name\":\"Baked French Toast\",\"description\":\"a delicious breakfast dish of egg-soaked bread\",\"ingredients\":\"6 slices of bread/r/n3 eggs/r/n3/4 cup of milk/r/nSalt/r/nButter\",\"cookingTime\":\"30 minutes\"}";
    }

    public String fetchMealResponse(String Type, String extendedPrompt)
            throws JsonMappingException, JsonProcessingException {
        JsonNode jsonNode = jsonMapper.readTree(getJSONResponse(Type, extendedPrompt));
        return jsonNode.get("text").asText();
    }

    public String getJSONResponse(String Type) throws JsonProcessingException {

        String prompt;
        String jsonRequest;

        prompt = pBuilder.buildPrompt(Type);
        jsonRequest = buildJsonApiRequest(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> request = new HttpEntity<String>(jsonRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_URL, request, String.class);

        return response.getBody();
    }

    public String getJSONResponse(String Type, String extendedPrompt) throws JsonProcessingException {

        String prompt;
        String jsonRequest;

        prompt = pBuilder.buildPrompt(Type, extendedPrompt);
        jsonRequest = buildJsonApiRequest(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> request = new HttpEntity<String>(jsonRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_URL, request, String.class);

        return response.getBody().replace("\\\"", "\"");
    }

    // build on predetermined prompt
    public String buildJsonApiRequest(String prompt) throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put("model", model);
        params.put("prompt", prompt);
        params.put("temperature", temperature);
        params.put("max_tokens", maximumTokenLength);
        params.put("stop", "####");
        // params.put("top_p", topP);
        params.put("frequency_penalty", freqPenalty);
        params.put("presence_penalty", presencePenalty);
        params.put("n", bestOfN);
        String res = new ObjectMapper().writeValueAsString(params);
        res += "\r\n";
        return res;
    }
    ///////////////////////////////////////////////////////////

    // setters ////////////////////////////////////////////////
    public void setModel(String model) {
        this.model = model;
    }

    public void setStop(String Stop) {
        this.stop = Stop;
    }

    public void setTemperature(double x) {
        this.temperature = x;
    }

    public void setTopP(double x) {
        this.topP = x;
    }

    public void setfreqPenalty(double x) {
        this.temperature = x;
    }

    public void setPresencePenalty(double x) {
        this.temperature = x;
    }

    public void setBestofN(int x) {
        this.bestOfN = x;
    }

    public int getBestofN() {
        return this.bestOfN;
    }
    //////////////////////////////////////////////////////////////

}
