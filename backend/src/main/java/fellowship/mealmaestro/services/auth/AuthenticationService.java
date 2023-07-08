package fellowship.mealmaestro.services.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.models.auth.AuthenticationRequestModel;
import fellowship.mealmaestro.models.auth.AuthenticationResponseModel;
import fellowship.mealmaestro.models.auth.AuthorityRoleModel;
import fellowship.mealmaestro.models.auth.RegisterRequestModel;
import fellowship.mealmaestro.repositories.UserRepository;

@Service
public class AuthenticationService {
    
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponseModel register(RegisterRequestModel request){
        var user = new UserModel(
            request.getFirstname(),
            passwordEncoder.encode(request.getPassword()),
            request.getEmail(),
            AuthorityRoleModel.USER
        );

        userRepository.createUser(user);

        var jwt = jwtService.generateToken(user);
        return new AuthenticationResponseModel(jwt);
    }

    public AuthenticationResponseModel authenticate(AuthenticationRequestModel request){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        var jwt = jwtService.generateToken(user);
        return new AuthenticationResponseModel(jwt);
    }
}
