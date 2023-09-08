package fellowship.mealmaestro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.mongo.findBarcodeRequest;
import fellowship.mealmaestro.models.mongo.FoodModelM;
import fellowship.mealmaestro.services.BarcodeService;

@RestController
public class ProductController {

    @Autowired
    private BarcodeService barcodeService;

    @PostMapping("/findProduct")
    public ResponseEntity<FoodModelM> findProduct(@RequestBody findBarcodeRequest request,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(barcodeService.findProduct(request));
    }

    @PostMapping("/addProduct")
    public ResponseEntity<FoodModelM> addProduct(@RequestBody FoodModelM product,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(barcodeService.addProduct(product));
    }
}
