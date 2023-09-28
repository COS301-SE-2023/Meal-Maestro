package fellowship.mealmaestro.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import fellowship.mealmaestro.models.auth.AuthenticationRequestModel;
import fellowship.mealmaestro.models.auth.AuthenticationResponseModel;
import fellowship.mealmaestro.models.auth.RegisterRequestModel;
import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.services.UserService;
import fellowship.mealmaestro.services.auth.AuthenticationService;
import fellowship.mealmaestro.services.auth.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "JWT_SECRET=secret",
        "DB_URI=bolt://localhost:7687",
        "DB_USERNAME=neo4j",
        "DB_PASSWORD=password"
})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private static JwtService jwtService;

    @Test
    public void findByEmailSuccessTest() throws Exception {
        UserModel userModel = new UserModel();

        when(userService.findByEmail(any(String.class))).thenReturn(java.util.Optional.of(userModel));

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/findByEmail")
                .with(user("user"))
                .contentType("application/json")
                .header("Authorization", "Bearer testToken..")
                .content("{\"name\":\"username\",\"password\":\"password\",\"email\":\"email\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void findByEmailFailureTest() throws Exception {

        when(userService.findByEmail(any(String.class))).thenReturn(Optional.empty());

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/findByEmail")
                .with(user("user"))
                .contentType("application/json")
                .header("Authorization", "Bearer testToken..")
                .content("{\"name\":\"username\",\"password\":\"password\",\"email\":\"email\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void registerSuccessTest() throws Exception {
        when(authenticationService.register(any(RegisterRequestModel.class))).thenReturn(Optional.of(new AuthenticationResponseModel("token")));

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/register")
                .contentType("application/json")
                .content("{\"username\":\"username\",\"email\":\"email\",\"password\":\"password\"}"))
            .andExpect(status().isOk());
    }

    @Test
    public void registerFailureTest() throws Exception {
        when(authenticationService.register(any(RegisterRequestModel.class))).thenReturn(Optional.empty());

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/register")
                .contentType("application/json")
                .content("{\"username\":\"username\",\"email\":\"email\",\"password\":\"password\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void authenticateSuccessTest() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequestModel.class))).thenReturn(new AuthenticationResponseModel("token"));

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/authenticate")
                .contentType("application/json")
                .content("{\"email\":\"email\",\"password\":\"password\"}"))
            .andExpect(status().isOk());
    }
}