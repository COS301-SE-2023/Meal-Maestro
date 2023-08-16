package fellowship.mealmaestro.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Node("Shopping List")
public class ShoppingListModel {
    @Id
    private UUID id;

    @Version
    private Long version;

    @Relationship(type = "IN_LIST")
    private List<FoodModel> foods;

    public ShoppingListModel() {
        this.id = UUID.randomUUID();
        this.foods = new ArrayList<FoodModel>();
    }
}
