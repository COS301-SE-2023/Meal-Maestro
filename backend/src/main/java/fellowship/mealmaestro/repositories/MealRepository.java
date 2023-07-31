package fellowship.mealmaestro.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import fellowship.mealmaestro.models.MealModel;

public interface MealRepository extends Neo4jRepository<MealModel,String>{
    
}
