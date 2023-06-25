package fellowship.mealmaestro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.services.MealManagementService;

@RestController
public class MealManagementController {
    @Autowired
    private MealManagementService mealManagementService;
}
