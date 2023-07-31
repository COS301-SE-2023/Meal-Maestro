package fellowship.mealmaestro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "JWT_SECRET=secret",
    "DB_URI=bolt://localhost:7687",
    "DB_USERNAME=neo4j",
    "DB_PASSWORD=password"
})
class MealmaestroApplicationTests {

	@Test
	void contextLoads() {
	}

}
