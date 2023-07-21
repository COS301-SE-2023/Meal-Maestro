package fellowship.mealmaestro.models;

import jakarta.validation.constraints.NotBlank;

public class DaysMealsModel {
    @NotBlank(message = "breakfast meal required")
    private String breakfast;

    @NotBlank(message = "lunch meal required")
    private String lunch;

    @NotBlank(message = "dinner meal required")
    private String dinner;

   

    public DaysMealsModel(String breakfast, String lunch,String dinner){
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
      
    }

    public String getBreakfast(){
        return this.breakfast;
    }

    public void setBreakfast(String breakfast){
        this.breakfast = breakfast;
    }

    public String getLunch(){
        return this.lunch;
    }

    public void setLunch(String lunch){
        this.lunch = lunch;
    }

    public String getDinner(){
        return this.dinner;
    }

    public void setDinner(String dinner){
        this.dinner = dinner;
    }
   
}
