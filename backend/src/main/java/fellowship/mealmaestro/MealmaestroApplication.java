package fellowship.mealmaestro;

import java.util.List;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MealmaestroApplication {
	private final Driver driver;

	@Autowired
	public MealmaestroApplication(Driver driver) {
		this.driver = driver;
	}
	public static void main(String[] args) {
		SpringApplication.run(MealmaestroApplication.class, args);
	}

	@GetMapping("/categories")
	public List<String> categories(){
		try (Session session = driver.session()){
			return session.executeRead(readCategories());
		}
	}

	private static TransactionCallback<List<String>> readCategories() {
		return transaction -> {
			Result result = transaction.run("MATCH (n:Category) RETURN n LIMIT 25;");
			return result.list(record -> record.get("n").asNode().get("categoryName").asString());
		};
	}
}
