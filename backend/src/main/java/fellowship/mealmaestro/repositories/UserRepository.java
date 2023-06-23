package fellowship.mealmaestro.repositories;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fellowship.mealmaestro.models.UserModel;

@Repository
public class UserRepository {
    
    @Autowired
    private final Driver driver;

    public UserRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public void createUser(UserModel user){
        try (Session session = driver.session()){

            session.executeWrite(createUserTransaction(user.getUsername(), user.getPassword(), user.getEmail()));
        }
    }

    public static TransactionCallback<Void> createUserTransaction(String username, String password, String email) {
        //creates user with default pantry, shopping list, recipe book, and preferences
        return transaction -> {
            transaction.run("CREATE (:Preferences)<-[:HAS_PREFERENCES]-(n0:User {username: $username, password: $password, email: $email})-[:HAS_PANTRY]->(:Pantry {capacity: 0}),\r\n" + //
                    "(:`Shopping List`)<-[:HAS_LIST]-(n0)-[:HAS_RECIPE_BOOK]->(:`Recipe Book`)",
            Values.parameters("username", username, "password", password, "email", email));
            return null;
        };
    }
    //#endregion

    //#region Check User
    public boolean checkUser(UserModel user){
        try (Session session = driver.session()){
            return session.executeRead(checkUserTransaction(user.getUsername(), user.getEmail()));
        }
    }

    public static TransactionCallback<Boolean> checkUserTransaction(String username, String email) {
        return transaction -> {
            var result = transaction.run("MATCH (n0:User {username: $username, email: $email}) RETURN n0",
            Values.parameters("username", username, "email", email));
            return result.hasNext();
        };
    }
    //#endregion
}

 
