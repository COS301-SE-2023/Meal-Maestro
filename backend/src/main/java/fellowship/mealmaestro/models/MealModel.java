package fellowship.mealmaestro.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

import jakarta.validation.constraints.NotBlank;

@Node("Meal")
public class MealModel {

    @Id
    @NotBlank(message = "A Meal Name is required")
    private String name;

    @NotBlank(message = "An image is required")
    private String image =  "https://images.unsplash.com/photo-1498837167922-ddd27525d352?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80";

    @NotBlank(message = "A Description is required")
    private String description;

    @NotBlank(message = "Ingredients are required")
    private String ingredients;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    @NotBlank(message = "Cooking time is required")
    private String cookingTime;
    public MealModel(){};

    public MealModel(String name, String instructions,String description, String image, String ingredients, String cookingTime){
        this.name = name;
        this.instructions = instructions;
        this.description = description;
        this.image = "https://images.unsplash.com/photo-1498837167922-ddd27525d352?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80";
        this.ingredients = ingredients;
        this.cookingTime = cookingTime;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getinstructions(){
        return this.instructions;
    }

    public void setinstructions(String instructions){
        this.instructions = instructions;
    }

    public String getdescription(){
        return this.description;
    }

    public void setdescription(String description){
        this.description = description;
    }

    public String getimage(){
        return this.image;
    }

    public void setimage(String image){
        this.image = image;
    }

    public String getingredients(){
        return this.ingredients;
    }

    public void setingredients(String ingredients){
        this.ingredients = ingredients;
    }

    public String getcookingTime(){
        return this.cookingTime;
    }

    public void setcookingTime(String cookingTime){
        this.cookingTime = cookingTime;
    }

    public void copyFromOtherModel(MealModel mealModel){
        this.name = mealModel.getName();
        this.cookingTime = mealModel.getcookingTime();
        this.ingredients = mealModel.getingredients();
        this.instructions = mealModel.getinstructions();
        this.description = mealModel.getdescription();
        this.image = mealModel.getimage();
    }
}
