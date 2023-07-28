package fellowship.mealmaestro.models;

import java.time.DayOfWeek;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;

@Node("DaysMeals")
public class DaysMealsModel {
    @Relationship(type = "breakfast")
    @NotBlank(message = "breakfast meal required")
    private MealModel breakfast;

    @Relationship(type = "lunch")
    @NotBlank(message = "lunch meal required")
    private MealModel lunch;

    @Relationship(type = "dinner")
    @NotBlank(message = "dinner meal required")
    private MealModel dinner;

    @Id
    @NotBlank(message = "date required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private DayOfWeek mealDate;
   
    @Relationship(type = "HAS_DAY")
    @NotBlank(message = "user required")
    private UserModel user;

    public DaysMealsModel(MealModel breakfast, MealModel lunch, MealModel dinner, DayOfWeek mealDate
   , UserModel user
    ){
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.mealDate = mealDate;
       this.user = user;
    }

    public MealModel getBreakfast(){
        return this.breakfast;
    }

    public void setBreakfast(MealModel breakfast){
        this.breakfast = breakfast;
    }

    public MealModel getLunch(){
        return this.lunch;
    }

    public void setLunch(MealModel lunch){
        this.lunch = lunch;
    }

    public MealModel getDinner(){
        return this.dinner;
    }

    public void setDinner(MealModel dinner){
        this.dinner = dinner;
    }
    
    public void setMealDate(DayOfWeek mealDate){
        this.mealDate = mealDate;
    }

    public DayOfWeek getMealDate(){
        return this.mealDate;
    }
}
