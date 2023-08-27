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
    private String image;

    @NotBlank(message = "A Description is required")
    private String description;

    @NotBlank(message = "Ingredients are required")
    private String ingredients;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    @NotBlank(message = "Cooking time is required")
    private String cookingTime;

    private String type;

    public MealModel() {
    };

    public MealModel(String name, String instructions, String description, String image, String ingredients,
            String cookingTime) {
        this.name = name;
        this.instructions = instructions;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
        this.cookingTime = cookingTime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCookingTime() {
        return this.cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String mealType) {
        this.type = mealType;
    }
}
