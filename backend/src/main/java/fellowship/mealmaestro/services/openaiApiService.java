package fellowship.mealmaestro.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class OpenaiApiService {
    Dotenv dotenv = Dotenv.configure().directory("backend\\.env").load();
    private static final String OPENAI_URL = "https://api.openai.com/v1/completions";

    private final String API_KEY = dotenv.get("OPENAI_API_KEY");
    private final RestTemplate restTemplate = new RestTemplate();

    private String model = "text-davinci-003";
    private String stop = "";

    private double temperature = 0.6;
    private double topP = 1.0;
    private double freqPenalty = 0.0;
    private double presencePenalty = 0.0;

    private int maximumTokenLength = 800;
    
    // potential vars
        // will make a few prompts and return best, heavy on token use
        private int bestOfN = 1;
        // detect abuse
        private String user = "";
        // echo back prompt and its compeletion
        private boolean echo = false;
        // stream prompt as it generates
        private boolean stream = false;

    @Autowired private ObjectMapper jsonMapper;
    
    public String getJSONResponse(String Type) throws JsonProcessingException{
       
        String prompt;
        String jsonRequest;

        OpenaiPromptBuilder pBuilder = new OpenaiPromptBuilder();
        prompt = pBuilder.buildPrompt(Type);
        jsonRequest = buildJsonApiRequest(prompt);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> request = new HttpEntity<String>(jsonRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_URL, request, String.class);
        
        return response.getBody().replace("\\\"", "\"");
    }


    // puts together prompt from common best practises
    public String buildPrompt(String context, String goal, String format, String secondaryTasks, String examples) {
        String Prompt = context + "\r\r" + goal + "\r\r" + format + "\r\r" + secondaryTasks + "\r\r"
                + examples;

        return Prompt;
    }

    // builds the json styles api request string /////////////////
    public String buildJsonApiRequest() throws JsonProcessingException {

        String prompt = buildPrompt("", "", "", "", "");

        Map<String, Object> params = new HashMap<>();
        params.put("model", model);
        params.put("prompt", prompt);
        params.put("temperature", temperature);
        params.put("max_tokens", maximumTokenLength);
        params.put("stream", false);
        params.put("stop", "###");
        
      //  params.put("top_p", topP);
        params.put("frequency_penalty", freqPenalty);
        params.put("presence_penalty", presencePenalty);
      //  params.put("n", bestOfN);

        return new ObjectMapper().writeValueAsString(params);
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
        res += "\r\n\r\n";
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
    //////////////////////////////////////////////////////////////

}
