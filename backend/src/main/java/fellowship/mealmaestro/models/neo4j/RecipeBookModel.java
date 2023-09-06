package fellowship.mealmaestro.models.neo4j;

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
@Node("Recipe Book")
public class RecipeBookModel {
    @Id
    private UUID id;

    @Version
    private Long version;

    @Relationship(type = "CONTAINS_RECIPE")
    private List<MealModel> recipes;

    public RecipeBookModel() {
        this.id = UUID.randomUUID();
        this.recipes = new ArrayList<MealModel>();
    }
}
