package fellowship.mealmaestro.models.neo4j;

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

    private double price;

    public FoodModel(String name, double quantity, String unit, double price, UUID id) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
    }

    public FoodModel() {
    }
}
