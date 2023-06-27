package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public void createUser(UserModel user){
        userRepository.createUser(user);
    }

    public boolean checkUser(UserModel user){
        //TODO: hash password
        return userRepository.checkUser(user);
    }

    public boolean login(UserModel user){
        return userRepository.login(user);
    }

    public UserModel getUser(UserModel user){
        return userRepository.getUser(user);
    }
}
