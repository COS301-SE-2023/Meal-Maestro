package fellowship.mealmaestro.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.models.UserModel;

public interface UserRepository extends Neo4jRepository<UserModel, String> {

    Optional<UserModel> findByEmail(String email);

    @Query("MATCH (n0:User {email: $email}) SET n0.name = $name RETURN n0")
    UserModel updateUser(String email, String username);

    @Query("MATCH (u:User {email: $email})-[r:HAS_MEAL]->(m:Meal {name: $oldMeal}) DELETE r WITH u MATCH (m2: Meal {name: $newMeal}) MERGE (u)-[r: HAS_MEAL {date: date($date)}]->(m2) return m2)")
    MealModel replaceMeal(LocalDate date, String oldMeal, String newMeal, String email);
}
