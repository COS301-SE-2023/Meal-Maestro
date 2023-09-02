package fellowship.mealmaestro.repositories;

import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import fellowship.mealmaestro.models.ShoppingListModel;

public interface ShoppingListRepository extends Neo4jRepository<ShoppingListModel, UUID> {

}
