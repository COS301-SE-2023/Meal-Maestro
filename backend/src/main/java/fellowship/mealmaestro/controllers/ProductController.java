package fellowship.mealmaestro.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.mongo.findBarcodeRequest;
import fellowship.mealmaestro.models.mongo.FoodModelM;
import fellowship.mealmaestro.services.BarcodeService;
// import fellowship.mealmaestro.services.webscraping.CheckersScraper;

@RestController
public class ProductController {

    private final BarcodeService barcodeService;
    // private final CheckersScraper checkersScraper;

    public ProductController(BarcodeService barcodeService) {
        this.barcodeService = barcodeService;
    }

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

    @GetMapping("/loc")
    public ResponseEntity<String> loc() {
        // checkersScraper.getLocLinks();
        return ResponseEntity.ok("loc");
    }
}
