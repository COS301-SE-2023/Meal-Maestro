package fellowship.mealmaestro.models;

public class UpdateUserRequestModel {
    String username;

    public UpdateUserRequestModel() {
    }

    public UpdateUserRequestModel(String username) {
        this.username = username;
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
