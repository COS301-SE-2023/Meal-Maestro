package fellowship.mealmaestro.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.repositories.UserRepository;
import fellowship.mealmaestro.services.auth.JwtService;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public Optional<UserModel> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserModel updateUser(UserModel user, String token) {
        String authToken = token.substring(7);
        String email = jwtService.extractUserEmail(authToken);
        return userRepository.updateUser(user, email);
    }

    public UserModel getUser(String token) {
        String authToken = token.substring(7);
        String email = jwtService.extractUserEmail(authToken);
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
