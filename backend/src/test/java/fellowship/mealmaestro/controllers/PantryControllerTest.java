package fellowship.mealmaestro.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import fellowship.mealmaestro.models.FoodModel;
import fellowship.mealmaestro.services.PantryService;
import fellowship.mealmaestro.services.auth.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
public class PantryControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PantryService pantryService;

    @MockBean
    private static JwtService jwtService;


    @Test
    public void addToPantrySuccessTest() throws Exception {
        FoodModel foodModel = new FoodModel("testFood", 2, 2);

        // When addToPantry method is called with any FoodModel and any String, it returns foodModel
        when(pantryService.addToPantry(any(FoodModel.class), any(String.class))).thenReturn(foodModel);

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/addToPantry")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer testToken..")
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
            .andExpect(status().isOk());
    }

    @Test
    public void addToPantryBadRequestTest() throws Exception {
        FoodModel foodModel = new FoodModel("testFood", 2, 2);

        // When addToPantry method is called with any FoodModel and any String, it returns foodModel
        when(pantryService.addToPantry(any(FoodModel.class), any(String.class))).thenReturn(foodModel);

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/addToPantry")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void removeFromPantrySuccessTest() throws Exception {
        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/removeFromPantry")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer testToken..")
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
            .andExpect(status().isOk());
    }

    @Test
    public void removeFromPantryBadRequestTest() throws Exception {
        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/removeFromPantry")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void updatePantrySuccessTest() throws Exception {
        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);
        
        mockMvc.perform(post("/updatePantry")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer testToken..")
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
            .andExpect(status().isOk());
    }

    @Test
    public void updatePantryBadRequestTest() throws Exception {
        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);
        
        mockMvc.perform(post("/updatePantry")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void getPantrySuccessTest() throws Exception {
        List<FoodModel> foodModelList = new ArrayList<>();
        when(pantryService.getPantry(any(String.class))).thenReturn(foodModelList);

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);
        
        mockMvc.perform(post("/getPantry")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer testToken.."))
            .andExpect(status().isOk());                
    }

    @Test
    public void getPantryBadRequestTest() throws Exception {
        List<FoodModel> foodModelList = new ArrayList<>();
        when(pantryService.getPantry(any(String.class))).thenReturn(foodModelList);

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);
        
        mockMvc.perform(post("/getPantry")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());                
    }
}
