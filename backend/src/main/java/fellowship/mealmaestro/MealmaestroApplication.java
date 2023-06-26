package fellowship.mealmaestro;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;


@SpringBootApplication
public class MealmaestroApplication {

	public static void main(String[] args) throws JsonProcessingException {

		SpringApplication.run(MealmaestroApplication.class, args);
	}
}
