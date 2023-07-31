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

    @Autowired private ObjectMapper jsonMapper = new ObjectMapper();
    @Autowired private OpenaiPromptBuilder pBuilder = new OpenaiPromptBuilder();
    
    public String fetchMealResponse(String Type) throws JsonMappingException, JsonProcessingException{
    //     String jsonResponse = getJSONResponse(Type);
    //     JsonNode jsonNode = jsonMapper.readTree(jsonResponse);
       
    //     String text = jsonNode.get("choices").get(0).get("text").asText();
    //    text = text.replace("\\\"", "\"");
    //     text = text.replace("\n", "");
    //     return text;

    return "[{\"instructions\":\"1. Preheat oven to 375 degrees\\n2. Grease a baking dish with butter\\n3. Beat together the eggs, milk, and a pinch of salt\\n4. Place the bread slices in the baking dish and pour the egg mixture over them\\n5. Bake in the preheated oven for 25 minutes\\n6. Serve warm with your favorite toppings\",\"name\":\"Baked French Toast\",\"description\":\"a delicious breakfast dish of egg-soaked bread\",\"ingredients\":\"6 slices of bread\\n3 eggs\\n3/4 cup of milk\\nSalt\\nButter\",\"cookingTime\":\"30 minutes\",\"url\":\"https://images.unsplash.com/photo-1484723091739-30a097e8f929?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=449&q=80\"},"
               + "{\"instructions\":\"Your instructions for the second entity\",\"name\":\"Chicken Pizza\",\"description\":\"Delicious indian pizza topped with succulent chicken and a variety of toppings.\",\"ingredients\":\"Ingredient 1\\nIngredient 2\\nIngredient 3\",\"cookingTime\":\"45 minutes\",\"url\":\"https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=481&q=80\"},"
               + "{\"instructions\":\"Your instructions for the third entity\",\"name\":\"Fish Tacos\",\"description\":\"Tasty tacos filled with flavorful fish, fresh veggies, and zesty sauces.\",\"ingredients\":\"Ingredient A\\nIngredient B\\nIngredient C\",\"cookingTime\":\"60 minutes\",\"url\":\"https://images.unsplash.com/photo-1565299585323-38d6b0865b47?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=480&q=80\"},"
     + "{\"instructions\":\"Your instructions for the fourth entity\",\"name\":\"Creamy Butter Salmon\",\"description\":\"A creamy and buttery salmon dish that's rich in flavor and perfectly cooked.\",\"ingredients\":\"Ingredient 1\\nIngredient 2\\nIngredient 3\",\"cookingTime\":\"45 minutes\",\"url\":\"https://images.unsplash.com/photo-1574484284002-952d92456975?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80\"},"
               + "{\"instructions\":\"Your instructions for the fifth entity\",\"name\":\"Prawn Pasta\",\"description\":\"Mouthwatering pasta dish with succulent prawns, cherry tomatoes, and fresh basil in a savory sauce.\",\"ingredients\":\"Ingredient A\\nIngredient B\\nIngredient C\",\"cookingTime\":\"60 minutes\",\"url\":\"https://images.unsplash.com/photo-1563379926898-05f4575a45d8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80\"}]";
    
    
}

    public String fetchMealResponse(String Type,String extendedPrompt) throws JsonMappingException, JsonProcessingException{
        JsonNode jsonNode = jsonMapper.readTree(getJSONResponse(Type,extendedPrompt));
        return jsonNode.get("text").asText();
    }
   
   
    public String getJSONResponse(String Type) throws JsonProcessingException{
       
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
    public String getJSONResponse(String Type, String extendedPrompt) throws JsonProcessingException{
       
        String prompt;
        String jsonRequest;

        
        prompt = pBuilder.buildPrompt(Type,extendedPrompt);
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
    //    params.put("top_p", topP);
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
