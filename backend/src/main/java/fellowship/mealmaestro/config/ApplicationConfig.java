package fellowship.mealmaestro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fellowship.mealmaestro.services.UserService;

@Configuration
public class ApplicationConfig {
    
    private final UserService userService;

    public ApplicationConfig(UserService userService){
        this.userService = userService;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userService.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }
}
