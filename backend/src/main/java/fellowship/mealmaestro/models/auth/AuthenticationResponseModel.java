package fellowship.mealmaestro.models.auth;

public class AuthenticationResponseModel {

    private String token;

    public AuthenticationResponseModel() {
    }

    public AuthenticationResponseModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthenticationResponseModel [token=" + token + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuthenticationResponseModel other = (AuthenticationResponseModel) o;
        return token != null ? token.equals(other.token) : other.token == null;
    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }
}
