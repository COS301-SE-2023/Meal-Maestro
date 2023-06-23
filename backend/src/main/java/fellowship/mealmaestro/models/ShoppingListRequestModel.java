package fellowship.mealmaestro.models;

import jakarta.validation.constraints.NotNull;

public class ShoppingListRequestModel {

    @NotNull(message = "User cannot be null")
    private UserModel user;

    @NotNull(message = "Food cannot be null")
    private FoodModel food;

    public ShoppingListRequestModel(UserModel user, FoodModel food){
        this.user = user;
        this.food = food;
    }

    public UserModel getUser(){
        return user;
    }

    public void setUser(UserModel user){
        this.user = user;
    }

    public FoodModel getFood(){
        return food;
    }

    public void setFood(FoodModel food){
        this.food = food;
    }
}
