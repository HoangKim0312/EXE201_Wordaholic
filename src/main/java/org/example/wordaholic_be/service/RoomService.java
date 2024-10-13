package org.example.wordaholic_be.service;

import org.example.wordaholic_be.dto.RoomCreateDto;
import org.example.wordaholic_be.dto.RoomDto;

public interface RoomService {
    RoomDto createRoom(RoomCreateDto roomCreateDto);
    void deleteRoom(Long roomId);
    RoomDto addPlayerToRoom(Long roomId, Long userId);
    RoomDto removePlayerFromRoom(Long roomId, Long userId);
    RoomDto changeGameStatus(Long roomId, boolean isPlaying);
}
