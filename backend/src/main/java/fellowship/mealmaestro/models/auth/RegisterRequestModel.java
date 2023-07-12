package fellowship.mealmaestro.models.auth;

public class RegisterRequestModel {
    
    private String firstname;
    private String email;
    private String password;

    public RegisterRequestModel(String firstname, String email, String password){
        this.firstname = firstname;
        this.email = email;
        this.password = password;
    }

    public String getFirstname(){
        return firstname;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
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
