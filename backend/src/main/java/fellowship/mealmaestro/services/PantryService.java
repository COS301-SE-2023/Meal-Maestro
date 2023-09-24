package fellowship.mealmaestro.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fellowship.mealmaestro.models.neo4j.FoodModel;
import fellowship.mealmaestro.models.neo4j.PantryModel;
import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.repositories.neo4j.FoodRepository;
import fellowship.mealmaestro.repositories.neo4j.PantryRepository;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class PantryService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PantryRepository pantryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Transactional
    public FoodModel addToPantry(FoodModel food, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        PantryModel pantry = user.getPantry();

        pantry.getFoods().add(food);
        pantryRepository.save(pantry);

        return food;
    }

    @Transactional
    public void removeFromPantry(FoodModel food, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        PantryModel pantry = user.getPantry();

        if (pantry.getFoods() == null) {
            return;
        }
        pantry.getFoods().removeIf(f -> f.getId().equals(food.getId()));
        foodRepository.deleteById(food.getId());

        pantryRepository.save(pantry);
    }

    @Transactional
    public void updatePantry(FoodModel food, String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        PantryModel pantry = user.getPantry();

        if (pantry.getFoods() == null) {
            return;
        }

        for (FoodModel f : pantry.getFoods()) {
            if (f.getId().equals(food.getId())) {
                f.setQuantity(food.getQuantity());
                f.setUnit(food.getUnit());
                break;
            }
        }

        pantryRepository.save(pantry);
    }

    @Transactional
    public List<FoodModel> getPantry(String token) {
        String email = jwtService.extractUserEmail(token);

        UserModel user = userRepository.findByEmail(email).get();
        PantryModel pantry = user.getPantry();

        if (pantry.getFoods() == null) {
            return new ArrayList<>();
        }

        return pantry.getFoods();
    }
}
