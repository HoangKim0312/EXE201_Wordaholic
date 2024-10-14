package org.example.wordaholic_be.service;

import org.example.wordaholic_be.dto.WordDto;

public interface GameService {
    WordDto startGame(String email);
    WordDto playTurn(String email, String word);
    WordDto botTurn();
}
