package fellowship.mealmaestro.models.neo4j.relationships;

import java.time.LocalDate;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import fellowship.mealmaestro.models.neo4j.MealModel;

@RelationshipProperties
public class HasLogEntry {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private MealModel meal;

    private LocalDate date;

    private String entryType;

    public HasLogEntry() {
    }

    public HasLogEntry(MealModel meal, LocalDate date, String entryType) {
        this.meal = meal;
        this.date = date;
        this.entryType = entryType;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MealModel getMeal() {
        return this.meal;
    }

    public void setMeal(MealModel meal) {
        this.meal = meal;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEntryType() {
        return this.entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }
}
