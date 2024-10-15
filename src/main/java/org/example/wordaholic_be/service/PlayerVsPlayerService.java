package org.example.wordaholic_be.service;

import org.example.wordaholic_be.dto.WordDto;

public interface PlayerVsPlayerService {
    WordDto startGame(String player1Email, String player2Email);
    WordDto playerTurn(String email, String word);
}
