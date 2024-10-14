package org.example.wordaholic_be.controller;

import org.example.wordaholic_be.dto.WordDto;
import org.example.wordaholic_be.service.GameService;
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
    @GetMapping("/api/game/bot-turn")
    public WordDto botTurn() {
        return gameService.botTurn();
    }
}