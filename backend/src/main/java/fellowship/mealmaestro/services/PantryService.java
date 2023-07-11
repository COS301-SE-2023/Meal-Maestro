package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.PantryRequestModel;
import fellowship.mealmaestro.repositories.PantryRepository;

@Service
public class PantryService {
    
    @Autowired
    private PantryRepository pantryRepository;

    public FoodModel addToPantry(PantryRequestModel request){
        return pantryRepository.addToPantry(request);
    }

    public void removeFromPantry(PantryRequestModel request){
        pantryRepository.removeFromPantry(request);
    }

    public void updatePantry(PantryRequestModel request){
        pantryRepository.updatePantry(request);
    }

    public List<FoodModel> getPantry(String token){
        return pantryRepository.getPantry(token);
    }
}
