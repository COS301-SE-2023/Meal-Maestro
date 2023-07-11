package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.ShoppingListRequestModel;
import fellowship.mealmaestro.services.ShoppingListService;
import jakarta.validation.Valid;

@RestController
public class ShoppingListController {
    
    @Autowired
    private ShoppingListService shoppingListService;

    @PostMapping("/addToShoppingList")
    public ResponseEntity<FoodModel> addToShoppingList(@Valid @RequestBody ShoppingListRequestModel request){
        if (request.getToken() == null || request.getToken().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(shoppingListService.addToShoppingList(request));
    }

    @PostMapping("/removeFromShoppingList")
    public ResponseEntity<Void> removeFromShoppingList(@Valid @RequestBody ShoppingListRequestModel request){
        if (request.getToken() == null || request.getToken().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        shoppingListService.removeFromShoppingList(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateShoppingList")
    public ResponseEntity<Void> updateShoppingList(@Valid @RequestBody ShoppingListRequestModel request){
        if (request.getToken() == null || request.getToken().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        shoppingListService.updateShoppingList(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/getShoppingList")
    public ResponseEntity<List<FoodModel>> getShoppingList(@RequestBody String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(shoppingListService.getShoppingList(token));
    }
}
