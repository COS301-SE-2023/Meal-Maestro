package fellowship.mealmaestro.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import fellowship.mealmaestro.models.MealModel;

public interface MealRepository extends Neo4jRepository<MealModel, String> {

        @Query("MATCH (m:Meal)\n" +
                        "WITH m, rand() as random\n" +
                        "ORDER BY random\n" +
                        "RETURN m.name AS name, m.instructions AS instructions, m.description AS description, " +
                        "m.image AS image, m.ingredients AS ingredients, m.cookingTime AS cookingTime, m.type AS type")
        List<MealModel> getPopularMeals(); // TODO: add limit and popularity

        @Query("MATCH (m:Meal)\n" +
                        "WHERE m.name CONTAINS $mealName\n" +
                        "RETURN m.name AS name, m.instructions AS instructions, m.description AS description, " +
                        "m.image AS image, m.ingredients AS ingredients, m.cookingTime AS cookingTime, m.type AS type")
        List<MealModel> getSearchedMeals(String mealName);

        @Query("MATCH (m:Meal)\n" +
                        "WITH m, rand() as random\n" +
                        "ORDER BY random\n" +
                        "LIMIT 100\n" +
                        "RETURN m.name AS name, m.instructions AS instructions, m.description AS description, " +
                        "m.image AS image, m.ingredients AS ingredients, m.cookingTime AS cookingTime, m.type AS type")
        List<MealModel> get100RandomMeals();

        @Query("MATCH (u:User {email: $email})-[r:HAS_MEAL]->(m:Meal {name: $oldMeal}) DELETE r WITH u MATCH (m2: Meal {name: $newMeal}) MERGE (u)-[r: HAS_MEAL {date: date($date)}]->(m2) RETURN m2.name AS name, m2.instructions AS instructions, m2.description AS description, "
                        +
                        "m2.image AS image, m2.ingredients AS ingredients, m2.cookingTime AS cookingTime, m2.type AS type")
        MealModel replaceMeal(LocalDate date, String oldMeal, String newMeal, String email);
}
