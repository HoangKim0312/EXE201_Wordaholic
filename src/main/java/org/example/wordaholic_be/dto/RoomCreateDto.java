package org.example.wordaholic_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomCreateDto {
    private String roomName;
    private int maxPlayers;
    private int timeLimit;
}
