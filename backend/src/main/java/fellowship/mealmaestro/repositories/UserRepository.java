package fellowship.mealmaestro.repositories;

import java.util.Optional;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.models.auth.AuthorityRoleModel;

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
        // Creates user with default pantry, shopping list, recipe book, and preferences
        return transaction -> {
            transaction.run("CREATE (:Pantry)<-[:HAS_PANTRY]-(n0:User {username: $username, password: $password, email: $email})-[:HAS_PREFERENCES]->(n1:Preferences)-[:HAS_ALLERGIES]->(:Allergies), " +
                    "(:`Shopping List`)<-[:HAS_LIST]-(n0)-[:HAS_RECIPE_BOOK]->(:`Recipe Book`), " +
                    "(:Interval)<-[:HAS_INTERVAL]-(n1)-[:HAS_GOAL]->(:Goal), " +
                    "(:`Calorie Goal`)<-[:HAS_CALORIE_GOAL]-(n1)-[:HAS_EATING_STYLE]->(:`Eating Style`), " +
                    "(:Macro {protein: 0,carbs: 0, fat: 0})<-[:HAS_MACRO]-(n1)-[:HAS_BUDGET]->(:Budget), " +
                    "(:BMI {height: 0, weight: 0, BMI: 0})<-[:HAS_BMI]-(n1)-[:HAS_COOKING_TIME]->(:`Cooking Time`)",
                    Values.parameters("username", username, "password", password, "email", email));
            return null;
        };
    }
    
    
    //#endregion

    //#region Get User
    public Optional<UserModel> findByEmail(String email){
        try (Session session = driver.session()){
            UserModel user = session.executeRead(findByEmailTransaction(email));
            return Optional.ofNullable(user);
        }
    }

    public static TransactionCallback<UserModel> findByEmailTransaction(String email) {
        return transaction -> {
            var result = transaction.run("MATCH (n0:User {email: $email}) RETURN n0",
            Values.parameters("email", email));

            if (!result.hasNext()) {
                return null;
            }

            var record = result.single();
            var node = record.get("n0");
            UserModel user = new UserModel(
                node.get("username").asString(), 
                node.get("password").asString(), 
                node.get("email").asString(), 
                AuthorityRoleModel.USER
            );
            return user;
        };
    }
}

 
