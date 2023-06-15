package fellowship.mealmaestro.repositories;

import java.util.List;

import org.neo4j.driver.Driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fellowship.mealmaestro.models.FoodModel;

@Repository
public class PantryRepository {

    @Autowired
    private final Driver driver;

    public PantryRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public void addToPantry(FoodModel food){
        //TODO
    }
    
    //#endregion

    //#region Read
    public List<FoodModel> getPantry(FoodModel food){
        //TODO
        return null;
    }
    //#endregion

    //#region Update
    public void updatePantry(FoodModel food){
        //TODO
    }
    //#endregion

    //#region Delete
    public void removeFromPantry(FoodModel food){
        //TODO
    }
    //#endregion
}
