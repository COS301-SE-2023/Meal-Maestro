package fellowship.mealmaestro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import fellowship.mealmaestro.models.neo4j.MealModel;
import fellowship.mealmaestro.services.LogService;
import jakarta.validation.Valid;

@RestController
public class LikeDislikeController {
    @Autowired
    private LogService logService;

    @PostMapping("/liked")
    public ResponseEntity<Void> liked(@Valid @RequestBody MealModel request,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        logService.logMeal(token, request, "like");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/disliked")
    public ResponseEntity<Void> disliked(@Valid @RequestBody MealModel request,
            @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        logService.logMeal(token, request, "dislike");

        return ResponseEntity.ok().build();
    }
}
