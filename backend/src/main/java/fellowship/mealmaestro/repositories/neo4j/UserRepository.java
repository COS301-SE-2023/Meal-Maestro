package fellowship.mealmaestro.repositories.neo4j;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.models.neo4j.relationships.HasLogEntry;

public interface UserRepository extends Neo4jRepository<UserModel, String> {

    Optional<UserModel> findByEmail(String email);

    @Query("MATCH (n0:User {email: $email}) SET n0.name = $name RETURN n0")
    UserModel updateUser(String email, String username);

    @Query("MATCH (u:User)-[r:HAS_LOG_ENTRY]->(logEntry) WHERE r.processed IS NULL OR NOT r.processed RETURN DISTINCT u")
    List<UserModel> findUsersWithNewLogEntries();

    @Query("MATCH (user:User {id: $user.id})-[:HAS_LOG_ENTRY]->(logEntry) WHERE NOT logEntry.processed RETURN logEntry")
    List<HasLogEntry> findUnprocessedLogEntriesForUser(UserModel user);
}
