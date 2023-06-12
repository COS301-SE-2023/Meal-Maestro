package fellowship.mealmaestro.repositories;

import java.util.List;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository {
    
    @Autowired
    private final Driver driver;

    public CategoryRepository(Driver driver){
        this.driver = driver;
    }

    public List<String> readCategories(){
        try (Session session = driver.session()){
            return session.executeRead(readCategoriesTransaction());
        }
    }

    private static TransactionCallback<List<String>> readCategoriesTransaction() {
        return transaction -> {
            Result result = transaction.run("MATCH (n) RETURN n LIMIT 25");
            return result.list(record -> record.get("n").asNode().get("name").asString());
        };
    }
}
