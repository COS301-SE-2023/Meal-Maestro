package fellowship.mealmaestro.repositories;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import fellowship.mealmaestro.models.neo4j.UserModel;

public interface UserRepository extends Neo4jRepository<UserModel, String> {

    Optional<UserModel> findByEmail(String email);

    @Query("MATCH (n0:User {email: $email}) SET n0.name = $name RETURN n0")
    UserModel updateUser(String email, String username);
}
