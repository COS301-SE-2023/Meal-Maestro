package fellowship.mealmaestro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.services.CategoryService;

@RestController
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<String> categories(){
        return categoryService.getCategories();
    }
}
