package fellowship.mealmaestro.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class Neo4jConfig {
    @Bean
    public Driver neo4jDriver() {
        String uri;
        String username;
        String password;
        Dotenv dotenv;

        if (System.getenv("DB_URI") != null) {
            uri = System.getenv("DB_URI");
            username = System.getenv("DB_USERNAME");
            password = System.getenv("DB_PASSWORD");

            return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        }

        try {
            dotenv = Dotenv.load();
            uri = dotenv.get("DB_URI");
            username = dotenv.get("DB_USERNAME");
            password = dotenv.get("DB_PASSWORD");
        } catch (Exception e){
            dotenv = Dotenv.configure()
                            .ignoreIfMissing()
                            .load();
            uri = "No DB URI Found";
            username = "No DB Username Found";
            password = "No DB Password Found";
        }

        return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }
}