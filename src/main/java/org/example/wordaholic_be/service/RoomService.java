package org.example.wordaholic_be.service;

import org.example.wordaholic_be.dto.CreateRoomDto;
import org.example.wordaholic_be.dto.RoomDto;
import org.example.wordaholic_be.dto.UpdateRoomStatusDto;

public interface RoomService {
    RoomDto createRoom(CreateRoomDto roomDto);
    void deleteRoom(String roomId);
    RoomDto addPlayerToRoom(String roomId, String userEmail);
    RoomDto removePlayerFromRoom(String roomId, String userEmail);
    RoomDto changeRoomStatus(String roomId, UpdateRoomStatusDto statusDto);
}
