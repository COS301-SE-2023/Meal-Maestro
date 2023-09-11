package fellowship.mealmaestro.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import fellowship.mealmaestro.models.mongo.VisitedLinkModel;

public interface VisitedLinkRepository extends MongoRepository<VisitedLinkModel, String> {

}
