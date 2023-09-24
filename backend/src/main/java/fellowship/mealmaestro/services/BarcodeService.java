package fellowship.mealmaestro.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.mongo.FoodModelM;
import fellowship.mealmaestro.models.mongo.findBarcodeRequest;
import fellowship.mealmaestro.repositories.mongo.FoodMRepository;

@Service
public class BarcodeService {

    private final FoodMRepository foodMRepository;

    public BarcodeService(FoodMRepository foodMRepository) {
        this.foodMRepository = foodMRepository;
    }

    public FoodModelM findProduct(findBarcodeRequest request) {
        System.out.println(request.getStore());
        Optional<FoodModelM> product = foodMRepository.findInDynamicCollection(request.getBarcode(),
                request.getStore());
        if (product.isEmpty()) {
            FoodModelM nullProduct = new FoodModelM();
            nullProduct.setName("");
            nullProduct.setQuantity(0);
            nullProduct.setUnit("pcs");
            return nullProduct;
        } else {
            return product.get();
        }
    }

    public FoodModelM addProduct(FoodModelM product) {
        return foodMRepository.saveInDynamicCollection(product);
    }
}
