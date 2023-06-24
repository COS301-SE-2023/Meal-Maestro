package fellowship.mealmaestro.services;

import org.springframework.stereotype.Service;

@Service
public class OpenaiPromptBuilder {

    public String buildPrompt(){
        String prompt = "";
        
        prompt += buildContext();
        prompt += buildGoal();
        prompt += buildFormat();
        prompt += buildSubtasks();
        prompt += buildExample();

        return prompt;
    }
    
    public String buildContext(){
        String res = "";

        return res;
    }

    public String buildGoal(){
        String res = "";

        return res;
    }

    public String buildFormat(){
        String res = "";

        return res;
    }

    public String buildSubtasks(){
        String res = "";

        return res;
    }

    public String buildExample(){
        String res = "";

        return res;
    }
}
