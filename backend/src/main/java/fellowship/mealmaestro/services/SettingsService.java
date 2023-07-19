package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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