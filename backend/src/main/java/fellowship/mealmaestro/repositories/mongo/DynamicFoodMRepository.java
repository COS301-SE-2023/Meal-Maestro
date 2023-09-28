package fellowship.mealmaestro.repositories.mongo;

import java.util.Optional;

import fellowship.mealmaestro.models.mongo.FoodModelM;

public interface DynamicFoodMRepository {
    FoodModelM saveInDynamicCollection(FoodModelM food);

    // find
    Optional<FoodModelM> findInDynamicCollection(String barcode, String store);
}