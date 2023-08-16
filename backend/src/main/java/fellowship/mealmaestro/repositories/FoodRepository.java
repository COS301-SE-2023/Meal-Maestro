package fellowship.mealmaestro.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import fellowship.mealmaestro.models.FoodModel;

public interface FoodRepository extends Neo4jRepository<FoodModel, String> {
    void deleteByName(String name);
}
