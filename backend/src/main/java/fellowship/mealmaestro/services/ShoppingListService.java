package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.repositories.ShoppingListRepository;

@Service
public class ShoppingListService {
    
    @Autowired
    private ShoppingListRepository shoppingListRepository;

    public void addToShoppingList(FoodModel food){
        shoppingListRepository.addToShoppingList(food);
    }

    public void removeFromShoppingList(FoodModel food){
        shoppingListRepository.removeFromShoppingList(food);
    }

    public void updateShoppingList(FoodModel food){
        shoppingListRepository.updateShoppingList(food);
    }

    public List<FoodModel> getShoppingList(FoodModel food){
        return shoppingListRepository.getShoppingList(food);
    }
}
