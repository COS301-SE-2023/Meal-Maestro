package fellowship.mealmaestro.repositories.neo4j;

import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import fellowship.mealmaestro.models.neo4j.PantryModel;

public interface PantryRepository extends Neo4jRepository<PantryModel, UUID> {
    @Query("MATCH (User{email: $email})-[:HAS_PANTRY]->(p:Pantry) " +
            "RETURN p")
    PantryModel findByEmail(String email);
}
