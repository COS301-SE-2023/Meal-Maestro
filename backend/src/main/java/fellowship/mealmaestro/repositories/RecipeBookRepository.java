package fellowship.mealmaestro.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.UUID;

import fellowship.mealmaestro.models.RecipeBookModel;

public interface RecipeBookRepository extends Neo4jRepository<RecipeBookModel, UUID> {

}