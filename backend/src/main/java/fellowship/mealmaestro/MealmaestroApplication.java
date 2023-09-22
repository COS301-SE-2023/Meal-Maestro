package fellowship.mealmaestro;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootApplication
@EnableScheduling
public class MealmaestroApplication {

	public static void main(String[] args) throws JsonProcessingException {

		SpringApplication.run(MealmaestroApplication.class, args);
	}
}
