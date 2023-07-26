package fellowship.mealmaestro.repositories;

import java.sql.Date;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import fellowship.mealmaestro.models.DaysMealsModel;

public interface DaysMealsRepository extends Neo4jRepository<DaysMealsModel,Date> {
    
}
