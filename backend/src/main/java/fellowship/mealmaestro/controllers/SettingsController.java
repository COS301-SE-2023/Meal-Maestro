package fellowship.mealmaestro.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingsController {


    @PostMapping("/getSettings")
    public ResponseEntity<SettingsModel> getSettings(@RequestHeader("Authorization") String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String authToken = token.substring(7);
        return ResponseEntity.ok(SettingsService.getSettings(authToken));
    }


    }




