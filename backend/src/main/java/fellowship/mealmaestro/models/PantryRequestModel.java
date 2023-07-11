package fellowship.mealmaestro.models;

import jakarta.validation.constraints.NotNull;

public class PantryRequestModel {
    @NotNull(message = "A Token is required")
    private String token;

    @NotNull(message = "A Food is required")
    private FoodModel food;

    public PantryRequestModel(String token, FoodModel food){
        this.token = token;
        this.food = food;
    }

    public String getToken(){
        return this.token;
    }

    public FoodModel getFood(){
        return this.food;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setFood(FoodModel food){
        this.food = food;
    }
}
