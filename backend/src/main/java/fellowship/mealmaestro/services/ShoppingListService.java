package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.ShoppingListRequestModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.repositories.ShoppingListRepository;

@Service
public class ShoppingListService {
    
    @Autowired
    private ShoppingListRepository shoppingListRepository;

    public FoodModel addToShoppingList(ShoppingListRequestModel request){
        return shoppingListRepository.addToShoppingList(request);
    }

    public void removeFromShoppingList(ShoppingListRequestModel request){
        shoppingListRepository.removeFromShoppingList(request);
    }

    public void updateShoppingList(ShoppingListRequestModel request){
        shoppingListRepository.updateShoppingList(request);
    }

    public List<FoodModel> getShoppingList(UserModel user){
        return shoppingListRepository.getShoppingList(user);
    }
}
