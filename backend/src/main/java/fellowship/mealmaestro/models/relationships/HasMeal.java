package fellowship.mealmaestro.models.relationships;

import java.time.LocalDate;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import fellowship.mealmaestro.models.MealModel;

@RelationshipProperties
public class HasMeal {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private MealModel meal;

    private LocalDate date;

    public HasMeal() {
    }

    public HasMeal(MealModel meal, LocalDate date) {
        this.meal = meal;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public MealModel getMeal() {
        return meal;
    }

    public void setMeal(MealModel meal) {
        this.meal = meal;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
