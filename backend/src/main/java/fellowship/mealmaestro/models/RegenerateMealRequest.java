package fellowship.mealmaestro.models;

import java.time.LocalDate;

import fellowship.mealmaestro.models.neo4j.MealModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegenerateMealRequest {
    private MealModel meal;
    private LocalDate date;

    public RegenerateMealRequest() {
    }

    public RegenerateMealRequest(MealModel meal, LocalDate date) {
        this.meal = meal;
        this.date = date;
    }
}
