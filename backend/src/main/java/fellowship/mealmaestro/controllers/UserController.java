package fellowship.mealmaestro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.models.auth.AuthenticationRequestModel;
import fellowship.mealmaestro.models.auth.AuthenticationResponseModel;
import fellowship.mealmaestro.models.auth.RegisterRequestModel;
import fellowship.mealmaestro.services.UserService;
import fellowship.mealmaestro.services.auth.AuthenticationService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private final AuthenticationService authenticationService;

    public UserController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/findByEmail")
    public UserModel findByEmail(@RequestBody UserModel user){
        return userService.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseModel> register(
        @RequestBody RegisterRequestModel request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseModel> authenticate(
        @RequestBody AuthenticationRequestModel request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
