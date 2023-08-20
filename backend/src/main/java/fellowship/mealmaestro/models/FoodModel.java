package fellowship.mealmaestro.models;

import java.util.UUID;

// import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Node("Food")
public class FoodModel {
    @Id
    private UUID id;

    private String name;

    private double quantity;

    private String unit;

    public FoodModel(String name, double quantity, String unit, UUID id) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public FoodModel() {
    }
}
