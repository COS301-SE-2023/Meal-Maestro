package fellowship.mealmaestro.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Node("Pantry")
public class PantryModel {
    @Id
    private UUID id;

    @Version
    private Long version;

    @Relationship(type = "IN_PANTRY")
    private List<FoodModel> foods;

    public PantryModel() {
        this.id = UUID.randomUUID();
        this.foods = new ArrayList<FoodModel>();
    }

    @Override
    public String toString() {
        String csv = foods.stream()
                .map(FoodModel::getName)
                .collect(Collectors.joining(","));

        return csv;
    }
}
