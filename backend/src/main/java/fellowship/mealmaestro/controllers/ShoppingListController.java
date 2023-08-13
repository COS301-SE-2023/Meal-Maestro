package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.services.ShoppingListService;
import jakarta.validation.Valid;

@RestController
public class ShoppingListController {
    
    @Autowired
    private ShoppingListService shoppingListService;

    @PostMapping("/addToShoppingList")
    public ResponseEntity<FoodModel> addToShoppingList(@Valid @RequestBody FoodModel request, @RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        return ResponseEntity.ok(shoppingListService.addToShoppingList(request, authToken));
    }

    @PostMapping("/removeFromShoppingList")
    public ResponseEntity<Void> removeFromShoppingList(@Valid @RequestBody FoodModel request, @RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        shoppingListService.removeFromShoppingList(request, authToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateShoppingList")
    public ResponseEntity<Void> updateShoppingList(@Valid @RequestBody FoodModel request, @RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        shoppingListService.updateShoppingList(request, authToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/getShoppingList")
    public ResponseEntity<List<FoodModel>> getShoppingList(@RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        return ResponseEntity.ok(shoppingListService.getShoppingList(authToken));
    }

    @PostMapping("/buyItem") 
    public ResponseEntity<List<FoodModel>> buyItem(@Valid @RequestBody FoodModel request, @RequestHeader("Authorization") String token){
        //Will move item from shopping list to pantry and return updated pantry
        
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        List<FoodModel> pantry = shoppingListService.buyItem(request, authToken);

        if (pantry == null) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.ok(pantry);
    }
}
