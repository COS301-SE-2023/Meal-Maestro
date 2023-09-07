package fellowship.mealmaestro.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.mongo.FoodModelM;
import fellowship.mealmaestro.repositories.mongo.FoodMRepository;

@Service
public class BarcodeService {

    @Autowired
    private FoodMRepository foodMRepository;

    public FoodModelM findProduct(String code) {
        System.out.println(code);
        Optional<FoodModelM> product = foodMRepository.findById(code);
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
        return foodMRepository.save(product);
    }
}
