package fellowship.mealmaestro.models.auth;

public class AuthenticationRequestModel {
    private String email;
    private String password;

    public AuthenticationRequestModel(){
    }

    public AuthenticationRequestModel(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }
}
