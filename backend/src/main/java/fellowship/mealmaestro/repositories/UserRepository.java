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

            session.executeWrite(createUserTransaction(user.getName(), user.getPassword(), user.getEmail()));
        }
    }

    public static TransactionCallback<Void> createUserTransaction(String username, String password, String email) {
        //creates user with default pantry, shopping list, recipe book, and preferences
        return transaction -> {
            transaction.run("CREATE (:Preferences)<-[:HAS_PREFERENCES]-(n0:User {username: $username, password: $password, email: $email})-[:HAS_PANTRY]->(:Pantry),\r\n" + //
                    "(:`Shopping List`)<-[:HAS_LIST]-(n0)-[:HAS_RECIPE_BOOK]->(:`Recipe Book`)",
            Values.parameters("username", username, "password", password, "email", email));
            return null;
        };
    }
    //#endregion

    //#region Check User
    public boolean checkUser(UserModel user){
        try (Session session = driver.session()){
            return session.executeRead(checkUserTransaction(user.getName(), user.getEmail()));
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

    //#region Login
    public boolean login(UserModel user){
        try (Session session = driver.session()){
            return session.executeRead(loginTransaction(user.getEmail(), user.getPassword()));
        }
    }

    public static TransactionCallback<Boolean> loginTransaction(String email, String password) {
        return transaction -> {
            var result = transaction.run("MATCH (n0:User {email: $email, password: $password}) RETURN n0",
            Values.parameters("email", email, "password", password));
            return result.hasNext();
        };
    }
    //#endregion

    //#region Get User
    public UserModel getUser(UserModel user){
        try (Session session = driver.session()){
            return session.executeRead(getUserTransaction(user.getEmail()));
        }
    }

    public static TransactionCallback<UserModel> getUserTransaction(String email) {
        return transaction -> {
            var result = transaction.run("MATCH (n0:User {email: $email}) RETURN n0",
            Values.parameters("email", email));
            var record = result.single();
            var node = record.get("n0");
            UserModel user = new UserModel(node.get("username").asString(), node.get("password").asString(), node.get("email").asString());
            return user;
        };
    }
}

 
