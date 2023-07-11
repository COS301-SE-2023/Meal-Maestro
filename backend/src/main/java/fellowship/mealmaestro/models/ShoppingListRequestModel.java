package fellowship.mealmaestro.models;

import jakarta.validation.constraints.NotNull;

public class ShoppingListRequestModel {

    @NotNull(message = "Token cannot be null")
    private String token;

    @NotNull(message = "Food cannot be null")
    private FoodModel food;

    public ShoppingListRequestModel(String token, FoodModel food){
        this.token = token;
        this.food = food;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public FoodModel getFood(){
        return food;
    }

    public void setFood(FoodModel food){
        this.food = food;
    }
}
