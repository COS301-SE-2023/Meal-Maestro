package fellowship.mealmaestro.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import fellowship.mealmaestro.models.neo4j.FoodModel;
import fellowship.mealmaestro.services.ShoppingListService;
import fellowship.mealmaestro.services.auth.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "JWT_SECRET=secret",
        "DB_URI=bolt://localhost:7687",
        "DB_USERNAME=neo4j",
        "DB_PASSWORD=password"
})
public class ShoppingListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingListService shoppingListService;

    @MockBean
    private static JwtService jwtService;

    @Test
    public void addToShoppingListSuccessTest() throws Exception {
        FoodModel foodModel = new FoodModel("testFood", 2, "testUnit", null);

        // When addToShoppingList method is called with any FoodModel and any String, it
        // returns foodModel
        when(shoppingListService.addToShoppingList(any(FoodModel.class), any(String.class))).thenReturn(foodModel);

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/addToShoppingList")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer testToken..")
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
                .andExpect(status().isOk());
    }

    @Test
    public void addToShoppingListBadRequestTest() throws Exception {
        FoodModel foodModel = new FoodModel("testFood", 2, "testUnit", null);

        // When addToShoppingList method is called with any FoodModel and any String, it
        // returns foodModel
        when(shoppingListService.addToShoppingList(any(FoodModel.class), any(String.class))).thenReturn(foodModel);

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/addToShoppingList")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeFromShoppingListSuccessTest() throws Exception {
        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/removeFromShoppingList")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer testToken..")
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
            .andExpect(status().isOk());
    }

    @Test
    public void removeFromShoppingListBadRequestTest() throws Exception {
        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/removeFromShoppingList")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateShoppingListSuccessTest() throws Exception {
        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/updateShoppingList")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer testToken..")
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
            .andExpect(status().isOk());
    }

    @Test
    public void updateShoppingListBadRequestTest() throws Exception {
        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/updateShoppingList")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getShoppingListSuccessTest() throws Exception {
        List<FoodModel> foodModelList = new ArrayList<>();
        when(shoppingListService.getShoppingList(any(String.class))).thenReturn(foodModelList);

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/getShoppingList")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer testToken.."))
                .andExpect(status().isOk());
    }

    @Test
    public void getShoppingListBadRequestTest() throws Exception {
        List<FoodModel> foodModelList = new ArrayList<>();
        when(shoppingListService.getShoppingList(any(String.class))).thenReturn(foodModelList);

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/getShoppingList")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void buyItemSuccessTest() throws Exception {
        List<FoodModel> foodModelList = new ArrayList<>();
        when(shoppingListService.buyItem(any(FoodModel.class), any(String.class))).thenReturn(foodModelList);

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/buyItem")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer testToken..")
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
                .andExpect(status().isOk());
    }

    @Test
    public void buyItemBadRequestTest() throws Exception {
        List<FoodModel> foodModelList = new ArrayList<>();
        when(shoppingListService.buyItem(any(FoodModel.class), any(String.class))).thenReturn(foodModelList);

        when(jwtService.extractUserEmail(any(String.class))).thenReturn("test@test.com");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken..");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/buyItem")
                .with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"testFood\",\"quantity\":2,\"weight\":2}"))
                .andExpect(status().isBadRequest());
    }

}
