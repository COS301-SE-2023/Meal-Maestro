package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.services.ShoppingListService;
import jakarta.validation.Valid;

@RestController
public class ShoppingListController {
    
    @Autowired
    private ShoppingListService shoppingListService;

    @PostMapping("/addToShoppingList")
    public void addToShoppingList(@Valid @RequestBody FoodModel food){
        shoppingListService.addToShoppingList(food);
    }

    @PostMapping("/removeFromShoppingList")
    public void removeFromShoppingList(@Valid @RequestBody FoodModel food){
        shoppingListService.removeFromShoppingList(food);
    }

    @PostMapping("/updateShoppingList")
    public void updateShoppingList(@Valid @RequestBody FoodModel food){
        shoppingListService.updateShoppingList(food);
    }

    @PostMapping("/getShoppingList")
    public List<FoodModel> getShoppingList(@Valid @RequestBody FoodModel food){
        return shoppingListService.getShoppingList(food);
    }
}
