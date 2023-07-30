package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.MealModel;
import fellowship.mealmaestro.repositories.BrowseRepository;
import fellowship.mealmaestro.services.auth.JwtService;


@Service
public class BrowseService {
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private BrowseRepository browseRepository;

    public List<MealModel> getPopularMeals(String token){
        String email = jwtService.extractUserEmail(token);
        return browseRepository.getPopularMeals(email);
    }

    public List<MealModel> searchMeals(String mealName, String token){
        String email = jwtService.extractUserEmail(token);
        return browseRepository.searchMeals(mealName,email);
    }

}
