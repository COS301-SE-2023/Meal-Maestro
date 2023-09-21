package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;

import fellowship.mealmaestro.repositories.neo4j.UserRepository;

public class RecommendationService {
    @Autowired
    private UserRepository userRepository;
}
