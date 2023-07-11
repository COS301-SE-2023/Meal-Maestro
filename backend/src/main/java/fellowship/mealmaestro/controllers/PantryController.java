package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.PantryRequestModel;
import fellowship.mealmaestro.services.PantryService;
import jakarta.validation.Valid;

@RestController
public class PantryController {
    
    @Autowired
    private PantryService pantryService;

    @PostMapping("/addToPantry")
    public ResponseEntity<FoodModel> addToPantry(@Valid @RequestBody PantryRequestModel pantryRequest){
        if (pantryRequest.getToken() == null || pantryRequest.getToken().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(pantryService.addToPantry(pantryRequest));
    }
    
    @PostMapping("/removeFromPantry")
    public ResponseEntity<Void> removeFromPantry(@Valid @RequestBody PantryRequestModel pantryRequest){
        if (pantryRequest.getToken() == null || pantryRequest.getToken().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        pantryService.removeFromPantry(pantryRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updatePantry")
    public ResponseEntity<Void> updatePantry(@Valid @RequestBody PantryRequestModel pantryRequest){
        if (pantryRequest.getToken() == null || pantryRequest.getToken().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        pantryService.updatePantry(pantryRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/getPantry")
    public ResponseEntity<List<FoodModel>> getPantry(@RequestBody String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(pantryService.getPantry(token));
    }

}
