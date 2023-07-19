package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fellowship.mealmaestro.repositories.SettingsRepository;


@Service
public class SettingsService {


    public SettingsModel getPantry(String token){
        String email = jwtService.extractUserEmail(token);
        return SettingsRepository.getSettings(email);
    }
}