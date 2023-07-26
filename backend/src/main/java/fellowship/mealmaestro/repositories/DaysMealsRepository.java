package fellowship.mealmaestro.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import fellowship.mealmaestro.models.DaysMealsModel;

public interface DaysMealsRepository extends Neo4jRepository<DaysMealsModel,Date> {
    @Query("MATCH (d:DaysMeals) WHERE d.mealDate >= $startDate AND d.mealDate <= datetime($startDate) + duration('P7D') RETURN d")
    List<DaysMealsModel> findMealsForNextWeek(Date startDate);
}
