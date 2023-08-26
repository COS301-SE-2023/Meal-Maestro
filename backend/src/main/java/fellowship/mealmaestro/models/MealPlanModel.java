package fellowship.mealmaestro.models;

import java.time.DayOfWeek;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.fasterxml.jackson.annotation.JsonFormat;

@Node("MealPlan")
public class MealPlanModel {

    @Relationship(type = "BREAKFAST")
    private MealModel breakfast;

    @Relationship(type = "LUNCH")
    private MealModel lunch;

    @Relationship(type = "DINNER")
    private MealModel dinner;

    @Id
    private String userDateIdentifier;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private DayOfWeek mealDate;

    @Relationship(type = "HAS_DAY", direction = Relationship.Direction.INCOMING)
    private UserModel user;

    private String meal;

    public MealPlanModel() {
    };

    public MealPlanModel(MealModel breakfast, MealModel lunch, MealModel dinner, DayOfWeek mealDate, UserModel user) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.mealDate = mealDate;
        this.user = user;
        this.userDateIdentifier = (user.getEmail() + mealDate.toString());
    }

    public MealModel getBreakfast() {
        return this.breakfast;
    }

    public void setBreakfast(MealModel breakfast) {
        this.breakfast = breakfast;
    }

    public MealModel getLunch() {
        return this.lunch;
    }

    public void setLunch(MealModel lunch) {
        this.lunch = lunch;
    }

    public MealModel getDinner() {
        return this.dinner;
    }

    public void setDinner(MealModel dinner) {
        this.dinner = dinner;
    }

    public void setMealDate(DayOfWeek mealDate) {
        this.mealDate = mealDate;
    }

    public DayOfWeek getMealDate() {
        return this.mealDate;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getMeal() {
        return this.meal;
    }

    public void setUserDateIdentifier(String asText) {
        this.userDateIdentifier = asText;
    }
}
