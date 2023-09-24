package fellowship.mealmaestro.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fellowship.mealmaestro.models.neo4j.FoodModel;
import fellowship.mealmaestro.models.neo4j.PantryModel;
import fellowship.mealmaestro.models.neo4j.ShoppingListModel;
import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.repositories.neo4j.FoodRepository;
import fellowship.mealmaestro.repositories.neo4j.PantryRepository;
import fellowship.mealmaestro.repositories.neo4j.ShoppingListRepository;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class ShoppingListService {

    private final JwtService jwtService;
    private final ShoppingListRepository shoppingListRepository;
    private final UserRepository userRepository;
    private final PantryRepository pantryRepository;
    private final FoodRepository foodRepository;

    public ShoppingListService(JwtService jwtService, ShoppingListRepository shoppingListRepository,
            UserRepository userRepository, PantryRepository pantryRepository, FoodRepository foodRepository) {
        this.jwtService = jwtService;
        this.shoppingListRepository = shoppingListRepository;
        this.userRepository = userRepository;
        this.pantryRepository = pantryRepository;
        this.foodRepository = foodRepository;
    }

    @Transactional
    public FoodModel addToShoppingList(FoodModel food, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        ShoppingListModel shoppingList = user.getShoppingList();

        shoppingList.getFoods().add(food);
        shoppingListRepository.save(shoppingList);

        return food;
    }

    @Transactional
    public void removeFromShoppingList(FoodModel food, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        ShoppingListModel shoppingList = user.getShoppingList();

        if (shoppingList.getFoods() == null) {
            return;
        }

        shoppingList.getFoods().removeIf(f -> f.getId().equals(food.getId()));
        foodRepository.deleteById(food.getId());

        shoppingListRepository.save(shoppingList);
    }

    @Transactional
    public void updateShoppingList(FoodModel food, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        ShoppingListModel shoppingList = user.getShoppingList();

        if (shoppingList.getFoods() == null) {
            return;
        }

        for (FoodModel f : shoppingList.getFoods()) {
            if (f.getId().equals(food.getId())) {
                f.setQuantity(food.getQuantity());
                f.setUnit(food.getUnit());
                break;
            }
        }

        shoppingListRepository.save(shoppingList);
    }

    @Transactional
    public List<FoodModel> getShoppingList(String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        ShoppingListModel shoppingList = user.getShoppingList();

        if (shoppingList.getFoods() == null) {
            return new ArrayList<>();
        }

        return shoppingList.getFoods();
    }

    @Transactional
    public List<FoodModel> buyItem(FoodModel food, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        ShoppingListModel shoppingList = user.getShoppingList();
        PantryModel pantry = user.getPantry();

        List<FoodModel> pantryFoods = pantry.getFoods();
        boolean existsInPantry = false;

        // check if food exists in pantry
        for (FoodModel pantryFood : pantryFoods) {
            if (pantryFood.getName().equals(food.getName())) {
                existsInPantry = true;
                break;
            }
        }

        if (existsInPantry) {
            // if food exists in pantry, update pantry food and delete shopping list food
            for (FoodModel pantryFood : pantryFoods) {
                if (pantryFood.getName().equals(food.getName())) {
                    // pantryFood.setQuantity(pantryFood.getQuantity() + food.getQuantity());

                    if (!pantryFood.getUnit().equals(food.getUnit())) {
                        if (pantryFood.getUnit().equals("g") && food.getUnit().equals("kg")) {
                            pantryFood.setQuantity(pantryFood.getQuantity() + (food.getQuantity() * 1000));
                        } else if (pantryFood.getUnit().equals("kg") && food.getUnit().equals("g")) {
                            pantryFood.setQuantity(pantryFood.getQuantity() + (food.getQuantity() / 1000));
                        } else if (pantryFood.getUnit().equals("ml") && food.getUnit().equals("l")) {
                            pantryFood.setQuantity(pantryFood.getQuantity() + (food.getQuantity() * 1000));
                        } else if (pantryFood.getUnit().equals("l") && food.getUnit().equals("ml")) {
                            pantryFood.setQuantity(pantryFood.getQuantity() + (food.getQuantity() / 1000));
                        } else {
                            throw new IllegalArgumentException(
                                    "Cannot convert " + pantryFood.getUnit() + " to " + food.getUnit());
                        }
                    } else {
                        pantryFood.setQuantity(pantryFood.getQuantity() + food.getQuantity());
                    }

                    if (pantryFood.getQuantity() >= 1000
                            && (pantryFood.getUnit().equals("g") || pantryFood.getUnit().equals("ml"))) {
                        pantryFood.setUnit(pantryFood.getUnit().equals("g") ? "kg" : "l");
                        pantryFood.setQuantity(pantryFood.getQuantity() / 1000);
                    } else if (pantryFood.getQuantity() < 1
                            && (pantryFood.getUnit().equals("kg") || pantryFood.getUnit().equals("l"))) {
                        pantryFood.setUnit(pantryFood.getUnit().equals("kg") ? "g" : "ml");
                        pantryFood.setQuantity(pantryFood.getQuantity() * 1000);
                    }
                    break;
                }
            }
            pantry.setFoods(pantryFoods);
            pantryRepository.save(pantry);
            shoppingList.getFoods().removeIf(f -> f.getId().equals(food.getId()));
            foodRepository.deleteById(food.getId());
            shoppingListRepository.save(shoppingList);
        } else {
            // if food does not exist in pantry, add shopping list food to pantry
            pantryFoods.add(food);
            pantryRepository.save(pantry);
            shoppingList.getFoods().removeIf(f -> f.getId().equals(food.getId()));
            shoppingListRepository.save(shoppingList);
        }

        return pantryFoods;
    }
}
