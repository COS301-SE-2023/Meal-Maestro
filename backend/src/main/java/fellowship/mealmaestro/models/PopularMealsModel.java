package fellowship.mealmaestro.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node("PopularMeals")
public class PopularMealsModel {
    @Relationship(type = "HAS_MEAL")
    private List<MealModel> popularMealList;

    @Id
    private String idString = "PopularMeals";

    public PopularMealsModel(){};

    public PopularMealsModel(List<MealModel> mealModels){
        this.popularMealList = mealModels;
    };

    public void setPopularMeals(List<MealModel> mealModels){
        this.popularMealList = mealModels;
    }
    public List<MealModel> getPopularMeals(){
        return this.popularMealList;
    }
}
