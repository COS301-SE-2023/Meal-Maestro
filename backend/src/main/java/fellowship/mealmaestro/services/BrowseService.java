package fellowship.mealmaestro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.repositories.MealRepository;

@Service
public class BrowseService {

    @Autowired
    private MealRepository mealRepository;

    public List<MealModel> getPopularMeals() {

        // return 5 random meals
        return mealRepository.getPopularMeals();
    }

    public List<MealModel> getSearchedMeals(String mealName) {

        // return meals that contain the mealName
        return mealRepository.getSearchedMeals(mealName);
    }

}
