package fellowship.mealmaestro.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import fellowship.mealmaestro.models.mongo.ToVisitLinkModel;

public interface ToVisitLinkRepository extends
                MongoRepository<ToVisitLinkModel, String> {

}
