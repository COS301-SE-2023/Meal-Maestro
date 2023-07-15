import java.util.List;

public class RecipeBookModel {

    private List<RecipeModel> recipes;

    public RecipeBookModel(List<RecipeModel> recipes) {
        this.recipes = recipes;
    }

    public List<RecipeModel> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeModel> recipes) {
        this.recipes = recipes;
    }
}