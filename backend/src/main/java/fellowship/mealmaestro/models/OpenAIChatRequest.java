package fellowship.mealmaestro.models;

import java.util.ArrayList;
import java.util.List;

public class OpenAIChatRequest {
    private String model;
    private List<Message> messages;
    private double temperature;
    private int max_tokens;

    public OpenAIChatRequest() {
        this.temperature = 1.1;
        this.max_tokens = 800;
        this.messages = new ArrayList<>();
    }

    public OpenAIChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
        this.temperature = 1.1;
        this.max_tokens = 800;
    }

    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    public static class Message {
        private String role;
        private String content;

        public Message() {
        }

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
