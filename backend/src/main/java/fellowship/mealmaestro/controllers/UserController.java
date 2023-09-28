package fellowship.mealmaestro.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.config.exceptions.UserNotFoundException;
import fellowship.mealmaestro.models.UpdateUserRequestModel;
import fellowship.mealmaestro.models.auth.AuthenticationRequestModel;
import fellowship.mealmaestro.models.auth.AuthenticationResponseModel;
import fellowship.mealmaestro.models.auth.RegisterRequestModel;
import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.services.UserService;
import fellowship.mealmaestro.services.auth.AuthenticationService;

@RestController
public class UserController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    public UserController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/findByEmail")
    public UserModel findByEmail(@RequestBody UserModel user) {
        return userService.findByEmail(user.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseModel> register(
            @RequestBody RegisterRequestModel request) {
        Optional<AuthenticationResponseModel> response = authenticationService.register(request);
        if (response.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response.get());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseModel> authenticate(
            @RequestBody AuthenticationRequestModel request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserModel> updateUser(
            @RequestBody UpdateUserRequestModel user,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.updateUser(user, token));
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserModel> getUser(
            @RequestHeader("Authorization") String token) {
        UserModel user = userService.getUser(token);
        user.setPassword("");
        return ResponseEntity.ok(user);
    }
}
