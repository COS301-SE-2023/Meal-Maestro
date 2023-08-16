package fellowship.mealmaestro.models;

import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Node("Food")
public class FoodModel {
    @Id
    private String name;

    @Version
    private Long version;

    private double quantity;

    private String unit;
}
