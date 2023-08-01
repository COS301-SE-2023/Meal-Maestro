package fellowship.mealmaestro.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import fellowship.mealmaestro.models.SettingsModel;
import fellowship.mealmaestro.services.SettingsService;

@SpringBootTest


public class SettingsControllerTest {

    @InjectMocks
    SettingsController settingsController;
    
    @Mock
    SettingsService settingsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testGetSettings() {
        when(settingsService.getSettings("validToken")).thenReturn(new SettingsModel());

        ResponseEntity<SettingsModel> responseEntity = settingsController.getSettings("Bearer validToken");
        
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
    
    @Test
    public void testUpdateSettings() {
        SettingsModel settings = new SettingsModel();
        settings.setUserHeight(180);
        settings.setUserWeight(75);
        
        ResponseEntity<Void> responseEntity = settingsController.updateSettings(settings, "Bearer validToken");
        
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
