package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.ShoppingListRequestModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.services.ShoppingListService;
import jakarta.validation.Valid;

@RestController
public class ShoppingListController {
    
    @Autowired
    private ShoppingListService shoppingListService;

    @PostMapping("/addToShoppingList")
    public void addToShoppingList(@Valid @RequestBody ShoppingListRequestModel request){
        shoppingListService.addToShoppingList(request);
    }

    @PostMapping("/removeFromShoppingList")
    public void removeFromShoppingList(@Valid @RequestBody ShoppingListRequestModel request){
        shoppingListService.removeFromShoppingList(request);
    }

    @PostMapping("/updateShoppingList")
    public void updateShoppingList(@Valid @RequestBody ShoppingListRequestModel request){
        shoppingListService.updateShoppingList(request);
    }

    @PostMapping("/getShoppingList")
    public List<FoodModel> getShoppingList(@Valid @RequestBody UserModel user){
        return shoppingListService.getShoppingList(user);
    }
}
