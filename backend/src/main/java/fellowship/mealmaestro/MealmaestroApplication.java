package fellowship.mealmaestro;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;

import fellowship.mealmaestro.services.MealManagementService;
import fellowship.mealmaestro.services.OpenaiApiService;

@SpringBootApplication
public class MealmaestroApplication {

	public static void main(String[] args) throws JsonProcessingException {
		MealManagementService ms = new MealManagementService();
		String res = ms.generateDaysMeals();
		SpringApplication.run(MealmaestroApplication.class, args);
	}
}
