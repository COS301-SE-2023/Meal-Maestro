package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fellowship.mealmaestro.models.SettingsModel;
import fellowship.mealmaestro.repositories.SettingsRepository;
import fellowship.mealmaestro.services.auth.JwtService;


@Service
public class SettingsService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SettingsRepository SettingsRepository;

    public SettingsModel getSettings(String token){
        String email = jwtService.extractUserEmail(token);
        return SettingsRepository.getSettings(email);
    }

    public void updateSettings(SettingsModel request, String token){
        String email = jwtService.extractUserEmail(token);
        SettingsRepository.updateSettings(request, email);
    }
}