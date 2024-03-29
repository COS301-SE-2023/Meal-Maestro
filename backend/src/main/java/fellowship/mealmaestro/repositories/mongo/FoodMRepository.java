package fellowship.mealmaestro.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import fellowship.mealmaestro.models.mongo.FoodModelM;

public interface FoodMRepository extends MongoRepository<FoodModelM, String>, DynamicFoodMRepository {
    FoodModelM findByBarcode(String barcode);
}
