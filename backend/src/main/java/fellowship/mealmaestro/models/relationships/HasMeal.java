package fellowship.mealmaestro.models.relationships;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import fellowship.mealmaestro.models.MealModel;

@RelationshipProperties
public class HasMeal {
    @Id
    UUID id;

    @TargetNode
    private MealModel meal;

    private LocalDate date;

    private String mealType; // breakfast, lunch, dinner

    public HasMeal() {
        this.id = UUID.randomUUID();
    }

    public HasMeal(MealModel meal, LocalDate date, String mealType) {
        this.id = UUID.randomUUID();
        this.meal = meal;
        this.date = date;
        this.mealType = mealType;
    }

    public UUID getId() {
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

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String type) {
        this.mealType = type;
    }
}
