package fellowship.mealmaestro.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.models.neo4j.UserModel;

import java.util.List;

@Node("SavedMeals")
public class SavedMealsModel {
    @Relationship(type = "SAVED_MEAL")
    private List<MealModel> savedMealList;

    @Relationship(type = "HAS_DAY", direction = Relationship.Direction.INCOMING)
    private UserModel user;

    @Id
    private String userSavedIdentifier;

    public SavedMealsModel() {
    };

    public SavedMealsModel(List<MealModel> savedMealList, UserModel user, String userSavedIdentifier) {
        this.savedMealList = savedMealList;
        this.user = user;
        this.userSavedIdentifier = userSavedIdentifier;
    };

    public List<MealModel> getMealModels() {
        return this.savedMealList;
    }

    public void setMealModels(List<MealModel> mealModels) {
        this.savedMealList = mealModels;
    }

    public UserModel getUserModel() {
        return this.user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getUserSavedIdentifier() {
        return this.userSavedIdentifier;
    }

    public void setUserDateIdentifier(String id) {
        this.userSavedIdentifier = id;
    }

}
