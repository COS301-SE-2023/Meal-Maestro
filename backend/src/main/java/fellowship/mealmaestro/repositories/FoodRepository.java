package fellowship.mealmaestro.repositories;

import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import fellowship.mealmaestro.models.FoodModel;

public interface FoodRepository extends Neo4jRepository<FoodModel, UUID> {

}
