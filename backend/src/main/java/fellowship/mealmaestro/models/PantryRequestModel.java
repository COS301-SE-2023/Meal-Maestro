package fellowship.mealmaestro.models;

import jakarta.validation.constraints.NotNull;

public class PantryRequestModel {
    @NotNull(message = "A User is required")
    private UserModel user;

    @NotNull(message = "A Food is required")
    private FoodModel food;

    public PantryRequestModel(UserModel user, FoodModel food){
        this.user = user;
        this.food = food;
    }

    public UserModel getUser(){
        return this.user;
    }

    public FoodModel getFood(){
        return this.food;
    }

    public void setUser(UserModel user){
        this.user = user;
    }

    public void setFood(FoodModel food){
        this.food = food;
    }
}
