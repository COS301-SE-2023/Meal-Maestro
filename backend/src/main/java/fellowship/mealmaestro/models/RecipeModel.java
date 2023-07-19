package fellowship.mealmaestro.models;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class RecipeModel {
    @NotBlank(message = "A title is required")
    private String title;

    @NotBlank(message = "An image is required")
    private String image;

    private List<RecipeModel> recipes;

    public RecipeModel(List<RecipeModel> recipes) {
        this.recipes = recipes;
    }

    public List<RecipeModel> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeModel> recipes) {
        this.recipes = recipes;
    }

    public String getTitle() {
        return this.title;
    }

    public getImage() {
        return this.getImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }
}