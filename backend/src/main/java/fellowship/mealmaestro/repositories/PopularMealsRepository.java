package fellowship.mealmaestro.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import fellowship.mealmaestro.models.PopularMealsModel;

public interface PopularMealsRepository extends Neo4jRepository<PopularMealsModel, String>{
    
}
