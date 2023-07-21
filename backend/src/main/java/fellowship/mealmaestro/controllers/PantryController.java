package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.services.PantryService;
import jakarta.validation.Valid;

@RestController
public class PantryController {
    
    @Autowired
    private PantryService pantryService;

    @PostMapping("/addToPantry")
    public ResponseEntity<FoodModel> addToPantry(@Valid @RequestBody FoodModel request, @RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        return ResponseEntity.ok(pantryService.addToPantry(request, authToken));
    }
    
    @PostMapping("/removeFromPantry")
    public ResponseEntity<Void> removeFromPantry(@Valid @RequestBody FoodModel request, @RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        pantryService.removeFromPantry(request, authToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updatePantry")
    public ResponseEntity<Void> updatePantry(@Valid @RequestBody FoodModel request, @RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        pantryService.updatePantry(request, authToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/getPantry")
    public ResponseEntity<List<FoodModel>> getPantry(@RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        return ResponseEntity.ok(pantryService.getPantry(authToken));
    }

}
