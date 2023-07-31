package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.repositories.ShoppingListRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class ShoppingListService {

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private ShoppingListRepository shoppingListRepository;

    public FoodModel addToShoppingList(FoodModel request, String token){
        String email = jwtService.extractUserEmail(token);
        return shoppingListRepository.addToShoppingList(request, email);
    }

    public void removeFromShoppingList(FoodModel request, String token){
        String email = jwtService.extractUserEmail(token);
        shoppingListRepository.removeFromShoppingList(request, email);
    }

    public void updateShoppingList(FoodModel request, String token){
        String email = jwtService.extractUserEmail(token);
        shoppingListRepository.updateShoppingList(request, email);
    }

    public List<FoodModel> getShoppingList(String token){
        String email = jwtService.extractUserEmail(token);
        return shoppingListRepository.getShoppingList(email);
    }

    public List<FoodModel> buyItem(FoodModel request, String token){
        String email = jwtService.extractUserEmail(token);
        return shoppingListRepository.buyItem(request, email);
    }
}
