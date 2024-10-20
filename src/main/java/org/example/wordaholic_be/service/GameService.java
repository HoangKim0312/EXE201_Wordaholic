package org.example.wordaholic_be.service;

import org.example.wordaholic_be.Response.PointsResponseDto;
import org.example.wordaholic_be.dto.WordDto;
import org.example.wordaholic_be.entity.Points;

public interface GameService {
    WordDto startGame(String email);
    WordDto playTurn(String email, String word);
    WordDto botTurn();
//    int endGame(String email);

    WordDto endGame(String email);
    PointsResponseDto checkPoints(String email);
}
