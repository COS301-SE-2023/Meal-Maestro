package fellowship.mealmaestro.repositories;

import java.util.List;

import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fellowship.mealmaestro.models.FoodModel;

@Repository
public class ShoppingListRepository {
    
    @Autowired
    private final Driver driver;

    public ShoppingListRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public void addToShoppingList(FoodModel food){
        //TODO
    }
    //#endregion

    //#region Read
    public List<FoodModel> getShoppingList(FoodModel food){
        //TODO
        return null;
    }
    //#endregion

    //#region Update
    public void updateShoppingList(FoodModel food){
        //TODO
    }
    //#endregion

    //#region Delete
    public void removeFromShoppingList(FoodModel food){
        //TODO
    }
    //#endregion
}
