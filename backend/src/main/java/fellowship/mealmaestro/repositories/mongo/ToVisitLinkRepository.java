package fellowship.mealmaestro.repositories.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import fellowship.mealmaestro.models.mongo.ToVisitLinkModel;

public interface ToVisitLinkRepository extends
                MongoRepository<ToVisitLinkModel, String> {

        Optional<ToVisitLinkModel> findNext(); // TODO: add query to find next link

}
