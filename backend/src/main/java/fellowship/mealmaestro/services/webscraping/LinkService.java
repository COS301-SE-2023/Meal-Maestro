package fellowship.mealmaestro.services.webscraping;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.mongo.ToVisitLinkModel;

@Service
public class LinkService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<ToVisitLinkModel> getNextLink() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sample(1));

        AggregationResults<ToVisitLinkModel> results = mongoTemplate.aggregate(aggregation, "ToVisitLinks",
                ToVisitLinkModel.class);

        List<ToVisitLinkModel> links = results.getMappedResults();

        return links.isEmpty() ? Optional.empty() : Optional.of(links.get(0));
    }
}
