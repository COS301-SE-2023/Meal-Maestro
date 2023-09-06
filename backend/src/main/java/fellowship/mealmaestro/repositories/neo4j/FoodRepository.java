package fellowship.mealmaestro.repositories.neo4j;

import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import fellowship.mealmaestro.models.neo4j.FoodModel;

public interface FoodRepository extends Neo4jRepository<FoodModel, UUID> {

}
