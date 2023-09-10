package fellowship.mealmaestro.services.webscraping;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.mongo.ToVisitLinkModel;

@Service
public class LinkService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<ToVisitLinkModel> getNextLink(String store) {
        MatchOperation match = Aggregation.match(Criteria.where("store").is(store));

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                Aggregation.sample(1));

        AggregationResults<ToVisitLinkModel> results = mongoTemplate.aggregate(aggregation, "ToVisitLinks",
                ToVisitLinkModel.class);

        List<ToVisitLinkModel> links = results.getMappedResults();

        return links.isEmpty() ? Optional.empty() : Optional.of(links.get(0));
    }

    public Optional<ToVisitLinkModel> getNextCheckersLink() {
        return getNextLink("Checkers");
    }

    public Optional<ToVisitLinkModel> getNextWoolworthsLink() {
        return getNextLink("Woolworths");
    }
}
