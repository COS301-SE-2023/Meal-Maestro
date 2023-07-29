package fellowship.mealmaestro.models;

import jakarta.validation.constraints.NotBlank;

public class RecipeModel {
    @NotBlank(message = "A title is required")
    private String title;

    @NotBlank(message = "An image is required")
    private String image;

    public RecipeModel(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImage() {
        return this.image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }
}