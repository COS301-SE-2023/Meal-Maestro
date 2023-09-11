package fellowship.mealmaestro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.neo4j.SettingsModel;
import fellowship.mealmaestro.services.SettingsService;
import jakarta.validation.Valid;

@RestController
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    @PostMapping("/getSettings")
    public ResponseEntity<SettingsModel> getSettings(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        return ResponseEntity.ok(settingsService.getSettings(authToken));
    }

    @PostMapping("/updateSettings")
    public ResponseEntity<Void> updateSettings(@Valid @RequestBody SettingsModel request,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        request.setUserBMI(request.getUserHeight(), request.getUserWeight());
        settingsService.updateSettings(request, authToken);
        return ResponseEntity.ok().build();
    }

}
