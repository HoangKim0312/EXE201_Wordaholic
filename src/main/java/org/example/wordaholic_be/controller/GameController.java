package org.example.wordaholic_be.controller;

import org.example.wordaholic_be.Response.PointsResponseDto;
import org.example.wordaholic_be.dto.WordDto;
import org.example.wordaholic_be.entity.Points;
import org.example.wordaholic_be.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public WordDto startGame(@RequestParam String email) {
        return gameService.startGame(email);
    }

    @PostMapping("/play")
    public WordDto playTurn(@RequestParam String email, @RequestParam String word) {
        return gameService.playTurn(email, word);
    }
    // Endpoint for bot's turn
    @GetMapping("/bot-turn")
    public WordDto botTurn() {
        return gameService.botTurn();
    }

    @PostMapping("/end")
    public WordDto endGame(@RequestParam String email) {
        return gameService.endGame(email);
    }

    @GetMapping("/checkPoints")
    public ResponseEntity<PointsResponseDto> checkPoints(@RequestParam String email) {
        PointsResponseDto userPoints = gameService.checkPoints(email);
        if (userPoints == null) {
            return ResponseEntity.notFound().build(); // User not found
        }
        return ResponseEntity.ok(userPoints); // Return the user's points
    }
}