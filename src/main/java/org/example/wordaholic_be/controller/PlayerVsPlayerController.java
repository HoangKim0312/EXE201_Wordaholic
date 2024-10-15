package org.example.wordaholic_be.controller;

import org.example.wordaholic_be.dto.WordDto;
import org.example.wordaholic_be.service.PlayerVsPlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game/vs-player")
public class PlayerVsPlayerController {

    private final PlayerVsPlayerService playerVsPlayerService;

    public PlayerVsPlayerController(PlayerVsPlayerService playerVsPlayerService) {
        this.playerVsPlayerService = playerVsPlayerService;
    }

    @PostMapping("/start")
    public ResponseEntity<WordDto> startGame(@RequestParam String player1Email, @RequestParam String player2Email) {
        WordDto wordDto = playerVsPlayerService.startGame(player1Email, player2Email);
        return ResponseEntity.ok(wordDto);
    }

    @PostMapping("/turn")
    public ResponseEntity<WordDto> playerTurn(@RequestParam String email, @RequestParam String word) {
        WordDto wordDto = playerVsPlayerService.playerTurn(email, word);
        return ResponseEntity.ok(wordDto);
    }
}
