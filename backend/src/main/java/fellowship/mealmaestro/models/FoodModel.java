package fellowship.mealmaestro.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class FoodModel {
    @NotBlank(message = "A Food Name is required")
    private String name;

    // private String category;

    @PositiveOrZero(message = "Quantity must be a positive number")
    private int quantity;

    @PositiveOrZero(message = "Weight must be a positive number")
    private int weight;

    public FoodModel(String name, int quantity, int weight){
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }


    public String getName(){
        return this.name;
    }

    // public String getCategory(){
    //     return this.category;
    // }

    public int getQuantity(){
        return this.quantity;
    }

    public int getWeight(){
        return this.weight;
    }

    public void setName(String name){
        this.name = name;
    }

    // public void setCategory(String category){
    //     this.category = category;
    // }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }
}
