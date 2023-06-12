package fellowship.mealmaestro.repositories;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    
    @Autowired
    private final Driver driver;

    public UserRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public void createUser(String username, String password){
        try (Session session = driver.session()){
            session.executeWrite(createUserTransaction(username, password));
        }
    }

    public static TransactionCallback<Void> createUserTransaction(String username, String password) {
        //creates user with default pantry, shopping list, recipe book, and preferences
        return transaction -> {
            transaction.run("CREATE (:Preferences)<-[:HAS_PREFERENCES]-(n0:User {name: $username, password: $password})-[:HAS_PANTRY]->(:Pantry {capacity: 0}),\r\n" + //
                    "(:`Shopping List`)<-[:HAS_LIST]-(n0)-[:HAS_RECIPE_BOOK]->(:`Recipe Book`)",
            Values.parameters("name", username, "password", password));
            return null;
        };
    }
    //#endregion

    //#region Check User
    public boolean checkUser(String username, String password){
        try (Session session = driver.session()){
            return session.executeRead(checkUserTransaction(username, password));
        }
    }

    public static TransactionCallback<Boolean> checkUserTransaction(String username, String password) {
        return transaction -> {
            var result = transaction.run("MATCH (n0:User {name: $username, password: $password}) RETURN n0",
            Values.parameters("name", username, "password", password));
            return result.hasNext();
        };
    }
    //#endregion
}

 
