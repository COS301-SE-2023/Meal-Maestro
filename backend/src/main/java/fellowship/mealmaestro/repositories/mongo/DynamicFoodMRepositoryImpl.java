package fellowship.mealmaestro.repositories.mongo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import fellowship.mealmaestro.models.mongo.FoodModelM;

public class DynamicFoodMRepositoryImpl implements DynamicFoodMRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public DynamicFoodMRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public FoodModelM saveInDynamicCollection(FoodModelM food) {
        return mongoTemplate.save(food, food.getStore());
    }

    @Override
    public Optional<FoodModelM> findInDynamicCollection(String barcode, String store) {
        FoodModelM m = mongoTemplate.findById(barcode, FoodModelM.class, store);
        return Optional.ofNullable(m);
    }
}
