package fellowship.mealmaestro;

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
        Dotenv dotenv = Dotenv.load();
        String uri = dotenv.get("DB_URI");
        String username = dotenv.get("DB_USERNAME");
        String password = dotenv.get("DB_PASSWORD");

        return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }
}
