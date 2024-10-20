package org.example.wordaholic_be.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomDto {
    private String roomId;
    private String roomName;
    private int maxPlayers;
    private boolean gameInProgress;
    private int currentPlayerIndex;
    private int timeLimit;
    private List<String> playersEmails;
}
