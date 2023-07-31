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

        if (System.getenv("DB_URI") != null) {
            uri = System.getenv("DB_URI");
            username = System.getenv("DB_USERNAME");
            password = System.getenv("DB_PASSWORD");

            return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        }
        
        Dotenv dotenv = Dotenv.load();
        uri = dotenv.get("DB_URI");
        username = dotenv.get("DB_USERNAME");
        password = dotenv.get("DB_PASSWORD");

        return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }
}