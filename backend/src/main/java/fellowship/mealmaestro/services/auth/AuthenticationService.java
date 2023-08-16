package fellowship.mealmaestro.services.auth;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.PantryModel;
import fellowship.mealmaestro.models.ShoppingListModel;
import fellowship.mealmaestro.models.UserModel;
import fellowship.mealmaestro.models.auth.AuthenticationRequestModel;
import fellowship.mealmaestro.models.auth.AuthenticationResponseModel;
import fellowship.mealmaestro.models.auth.AuthorityRoleModel;
import fellowship.mealmaestro.models.auth.RegisterRequestModel;
import fellowship.mealmaestro.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public Optional<AuthenticationResponseModel> register(RegisterRequestModel request) {
        var user = new UserModel(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                AuthorityRoleModel.USER);

        user.setPantry(new PantryModel());
        user.setShoppingList(new ShoppingListModel());

        boolean userExists = userRepository.findByEmail(request.getEmail()).isPresent();

        if (userExists) {
            return Optional.empty();
        }

        userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        return Optional.of(new AuthenticationResponseModel(jwt));
    }

    public AuthenticationResponseModel authenticate(AuthenticationRequestModel request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var jwt = jwtService.generateToken(user);
        return new AuthenticationResponseModel(jwt);
    }
}
