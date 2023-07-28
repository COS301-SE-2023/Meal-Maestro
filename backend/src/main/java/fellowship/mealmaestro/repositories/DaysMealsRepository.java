package fellowship.mealmaestro.repositories;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import fellowship.mealmaestro.models.DaysMealsModel;

public interface DaysMealsRepository extends Neo4jRepository<DaysMealsModel,DayOfWeek> {
    @Query("MATCH (d:DaysMeals) WHERE d.mealDate >= $startDate AND d.mealDate <= datetime($startDate) + duration('P4D') RETURN d")
    List<DaysMealsModel> findMealsForNextWeek(DayOfWeek startDate);

    @Query("MATCH (d:DaysMeals {mealDate: $mealDate}) RETURN d LIMIT 1")
    Optional<DaysMealsModel> findByMealDate(DayOfWeek mealDate);

    @Query("MATCH (d:DaysMeals {mealDate: $mealDate}) RETURN d")
    Optional<DaysMealsModel> findMealsForDate(DayOfWeek mealDate);

}
