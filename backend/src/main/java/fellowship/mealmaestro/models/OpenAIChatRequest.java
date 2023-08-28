package fellowship.mealmaestro.models;

import java.util.List;

public class OpenAIChatRequest {
    private String model;
    private List<Message> messages;
    private double temperature;
    private double max_tokens;

    public OpenAIChatRequest() {
        this.temperature = 0.5;
        this.max_tokens = 800;
    }

    public OpenAIChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
        this.temperature = 0.5;
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

    public double getMax_tokens() {
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

    public void setMax_tokens(double max_tokens) {
        this.max_tokens = max_tokens;
    }

    public static class Message {
        private String role;
        private String text;

        public Message(String role, String text) {
            this.role = role;
            this.text = text;
        }

        public String getRole() {
            return role;
        }

        public String getText() {
            return text;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
