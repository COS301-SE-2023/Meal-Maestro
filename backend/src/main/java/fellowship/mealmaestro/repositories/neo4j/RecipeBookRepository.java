package fellowship.mealmaestro.repositories.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import fellowship.mealmaestro.models.neo4j.RecipeBookModel;

import java.util.UUID;

public interface RecipeBookRepository extends Neo4jRepository<RecipeBookModel, UUID> {

}