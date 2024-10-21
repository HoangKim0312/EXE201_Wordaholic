package org.example.wordaholic_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomDto {
    private String roomName;
    private int maxPlayers;
    private int timeLimit;
}
