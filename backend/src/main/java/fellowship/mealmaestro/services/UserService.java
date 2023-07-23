package fellowship.mealmaestro.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public Optional<UserModel> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserModel updateUser(UserModel user) {
        return userRepository.updateUser(user);
    }
}
