package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.repositories.PantryRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class PantryService {

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private PantryRepository pantryRepository;

    public FoodModel addToPantry(FoodModel request, String token){
        String email = jwtService.extractUserEmail(token);
        return pantryRepository.addToPantry(request, email);
    }

    public void removeFromPantry(FoodModel request, String token){
        String email = jwtService.extractUserEmail(token);
        pantryRepository.removeFromPantry(request, email);
    }

    public void updatePantry(FoodModel request, String token){
        String email = jwtService.extractUserEmail(token);
        pantryRepository.updatePantry(request, email);
    }

    public List<FoodModel> getPantry(String token){
        String email = jwtService.extractUserEmail(token);
        return pantryRepository.getPantry(email);
    }
}
