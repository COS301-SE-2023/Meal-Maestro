package fellowship.mealmaestro.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.neo4j.FoodModel;
import fellowship.mealmaestro.services.PantryService;
import jakarta.validation.Valid;

@RestController
public class PantryController {

    private final PantryService pantryService;

    public PantryController(PantryService pantryService) {
        this.pantryService = pantryService;
    }

    @PostMapping("/addToPantry")
    public ResponseEntity<FoodModel> addToPantry(@Valid @RequestBody FoodModel request,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        request.setId(UUID.randomUUID());
        String authToken = token.substring(7);
        return ResponseEntity.ok(pantryService.addToPantry(request, authToken));
    }

    @PostMapping("/removeFromPantry")
    public ResponseEntity<Void> removeFromPantry(@Valid @RequestBody FoodModel request,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        pantryService.removeFromPantry(request, authToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updatePantry")
    public ResponseEntity<Void> updatePantry(@Valid @RequestBody FoodModel request,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        pantryService.updatePantry(request, authToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/getPantry")
    public ResponseEntity<List<FoodModel>> getPantry(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        return ResponseEntity.ok(pantryService.getPantry(authToken));
    }
}
