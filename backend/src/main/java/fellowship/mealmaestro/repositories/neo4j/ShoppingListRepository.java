package fellowship.mealmaestro.repositories.neo4j;

import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import fellowship.mealmaestro.models.neo4j.ShoppingListModel;

public interface ShoppingListRepository extends Neo4jRepository<ShoppingListModel, UUID> {

}
