package fellowship.mealmaestro.repositories;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.neo4j.driver.Values;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fellowship.mealmaestro.models.MealModel;


@Repository
public class BrowseRepository {
    
    @Autowired
    private final Driver driver;

    public BrowseRepository(Driver driver){
        this.driver = driver;
    }

    public List<MealModel> getPopularMeals() {
        try (Session session = driver.session()) {
            return session.readTransaction(this::getPopularMealsTransaction);
        }
    }

    public List<MealModel> getPopularMealsTransaction(Transaction tx) {
        List<MealModel> popularMeals = new ArrayList<>();
    
        org.neo4j.driver.Result result = tx.run("MATCH (m:Meal)<--(u:User)\n" +
                "WITH m, count(u) as popularity\n" +
                "ORDER BY popularity DESC\n" +
                "LIMIT 10\n" +
                "RETURN m.name AS name, m.recipe AS recipe");
    
        while (result.hasNext()) {
            org.neo4j.driver.Record record = result.next();
            String name = record.get("name").asString();
            String recipe = record.get("recipe").asString();
            popularMeals.add(new MealModel(name, recipe));
        }
    
        return popularMeals;
        }


    public MealModel searchMealByName(String mealName) {
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> searchMealByNameTransaction(tx, mealName));
        }
    }

    public MealModel searchMealByNameTransaction(Transaction tx, String mealName) {
        org.neo4j.driver.Result result = tx.run("MATCH (m:Meal {name: $name})\n" +
                "RETURN m.name AS name, m.recipe AS recipe", Values.parameters("name", mealName));

        if (result.hasNext()) {
            org.neo4j.driver.Record record = result.next();
            String name = record.get("name").asString();
            String recipe = record.get("recipe").asString();
            return new MealModel(name, recipe);
        }

        return null; // Meal with the given name not found.
    }


}
