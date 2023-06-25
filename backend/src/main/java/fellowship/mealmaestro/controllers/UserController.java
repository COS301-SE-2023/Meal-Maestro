package fellowship.mealmaestro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.services.UserService;
import jakarta.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/createUser")
    public void createUser(@Valid @RequestBody UserModel user){
        userService.createUser(user);
    }

    @PostMapping("/checkUser")
    public boolean checkUser(@Valid @RequestBody UserModel user){
        return userService.checkUser(user);
    }

    @PostMapping("/login")
    public boolean login(@Valid @RequestBody UserModel user){
        return userService.login(user);
    }
}
