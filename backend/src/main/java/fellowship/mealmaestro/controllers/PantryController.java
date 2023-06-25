package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.models.PantryRequestModel;
import fellowship.mealmaestro.services.PantryService;
import jakarta.validation.Valid;

@RestController
public class PantryController {
    
    @Autowired
    private PantryService pantryService;

    @PostMapping("/addToPantry")
    public FoodModel addToPantry(@Valid @RequestBody PantryRequestModel pantryRequest){
        return pantryService.addToPantry(pantryRequest);
    }
    
    @PostMapping("/removeFromPantry")
    public void removeFromPantry(@Valid @RequestBody PantryRequestModel pantryRequest){
        pantryService.removeFromPantry(pantryRequest);
    }

    @PostMapping("/updatePantry")
    public void updatePantry(@Valid @RequestBody PantryRequestModel pantryRequest){
        pantryService.updatePantry(pantryRequest);
    }

    @PostMapping("/getPantry")
    public List<FoodModel> getPantry(@RequestBody UserModel user){
        return pantryService.getPantry(user);
    }

}
