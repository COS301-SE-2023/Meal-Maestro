package fellowship.mealmaestro.models;

import jakarta.validation.constraints.NotBlank;

public class MealModel {
    @NotBlank(message = "A Meal Name is required")
    private String name;

    @NotBlank(message = "A Recipe is required")
    private String recipe;

    public MealModel(String name, String recipe){
        this.name = name;
        this.recipe = recipe;
    }

    public String getName(){
        return this.name;
    }

    public String getRecipe(){
        return this.recipe;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setRecipe(String recipe){
        this.recipe = recipe;
    }
}
