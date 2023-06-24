package fellowship.mealmaestro.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class OpenaiApiService {
    Dotenv dotenv = Dotenv.load();
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    private final String API_KEY = dotenv.get("OPENAI_API_KEY");
    private final RestTemplate restTemplate = new RestTemplate();

    private String model = "text-davinci-003";
    private String stop = "";

    private double temperature = 0.6;
    private double topP = 1.0;
    private double freqPenalty = 0.0;
    private double presencePenalty = 0.0;

    private int maximumTokenLength = 500;
    
    // potential vars
        // will make a few prompts and return best, heavy on token use
        private int bestOfN = 1;
        // detect abuse
        private String user = "";
        // echo back prompt and its compeletion
        private boolean echo = false;
        // stream prompt as it generates
        private boolean stream = false;

    public String getJSONResponse(){
       
        String prompt;
        String jsonRequest;

        OpenaiPromptBuilder pBuilder = new OpenaiPromptBuilder();
        prompt = pBuilder.buildPrompt();
        jsonRequest = buildJsonApiRequest(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> request = new HttpEntity<String>(jsonRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_URL, request, String.class);

        return response.getBody();
    }


    // puts together prompt from common best practises
    public String buildPrompt(String context, String goal, String format, String secondaryTasks, String examples) {
        String Prompt = context + "\r\n\r\n" + goal + "\r\n\r\n" + format + "\r\n\r\n" + secondaryTasks + "\r\n\r\n"
                + examples;

        return Prompt;
    }

    // builds the json styles api request string /////////////////
    public String buildJsonApiRequest() {

        String prompt = buildPrompt("", "", "", "", "");

        String jsonRequest = "{"

                + "\"" + "model" + "\":" + "\"" + model + "\","
                + "\"" + "prompt" + "\":" + "\"" + prompt + "\","

                + "\"" + "temperature" + "\":" + temperature + ","
                + "\"" + "max_tokens" + "\":" + maximumTokenLength + ","
                + "\"" + "top_p" + "\":" + topP + ","

                + "\"" + "frequency_penalty" + "\":" + freqPenalty + ","
                + "\"" + "presence_penalty" + "\":" + presencePenalty + ","

                + "\"" + "n" + "\":" + bestOfN + ""

                + "}";

        return jsonRequest;
    }

    // build on predetermined prompt
    public String buildJsonApiRequest(String prompt) {
        String jsonRequest = "{"

                + "\"" + "model" + "\":" + "\"" + model + "\","
                + "\"" + "prompt" + "\":" + "\"" + prompt + "\","
                // doubles and ints dont get qouted
                + "\"" + "temperature" + "\":" + temperature + ","
                + "\"" + "max_tokens" + "\":" + maximumTokenLength + ","
                + "\"" + "top_p" + "\":" + topP + ","

                + "\"" + "frequency_penalty" + "\":" + freqPenalty + ","
                + "\"" + "presence_penalty" + "\":" + presencePenalty

                + "}";

        return jsonRequest;
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
