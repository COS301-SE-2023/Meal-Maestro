package fellowship.mealmaestro.repositories.neo4j;

import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import fellowship.mealmaestro.models.neo4j.SettingsModel;

public interface SettingsRepository extends Neo4jRepository<SettingsModel, UUID> {

}