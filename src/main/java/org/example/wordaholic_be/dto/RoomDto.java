package org.example.wordaholic_be.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.wordaholic_be.entity.Room;

@Getter
@Setter
public class RoomDto {
    private Long roomId;
    private String roomName;
    private int maxPlayers;
    private int currentPlayers;
    private boolean gameInProgress;
    private int timeLimit;

    public RoomDto(Room room) {
        this.roomId = room.getRoomId();
        this.roomName = room.getRoomName();
        this.maxPlayers = room.getMaxPlayers();
        this.currentPlayers = (room.getPlayers() != null) ? room.getPlayers().size() : 0;
        this.gameInProgress = room.isGameInProgress();
        this.timeLimit = room.getTimeLimit();
    }
}
