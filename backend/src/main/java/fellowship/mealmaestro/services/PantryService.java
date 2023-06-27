package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.PantryRequestModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.repositories.PantryRepository;

@Service
public class PantryService {
    
    @Autowired
    private PantryRepository pantryRepository;

    public FoodModel addToPantry(PantryRequestModel pantryRequest){
        return pantryRepository.addToPantry(pantryRequest);
    }

    public void removeFromPantry(PantryRequestModel pantryRequest){
        pantryRepository.removeFromPantry(pantryRequest);
    }

    public void updatePantry(PantryRequestModel pantryRequest){
        pantryRepository.updatePantry(pantryRequest);
    }

    public List<FoodModel> getPantry(UserModel user){
        return pantryRepository.getPantry(user);
    }
}
