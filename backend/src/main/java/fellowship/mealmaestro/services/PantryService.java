package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.repositories.PantryRepository;

@Service
public class PantryService {
    
    @Autowired
    private PantryRepository pantryRepository;

    public void addToPantry(FoodModel food){
        pantryRepository.addToPantry(food);
    }

    public void removeFromPantry(FoodModel food){
        pantryRepository.removeFromPantry(food);
    }

    public void updatePantry(FoodModel food){
        pantryRepository.updatePantry(food);
    }

    public List<FoodModel> getPantry(FoodModel food){
        return pantryRepository.getPantry(food);
    }
}
