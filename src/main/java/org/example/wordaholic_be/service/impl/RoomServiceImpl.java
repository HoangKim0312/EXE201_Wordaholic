package org.example.wordaholic_be.service.impl;

import org.example.wordaholic_be.dto.RoomCreateDto;
import org.example.wordaholic_be.dto.RoomDto;
import org.example.wordaholic_be.entity.Room;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.repository.RoomRepository;
import org.example.wordaholic_be.repository.UserRepository;
import org.example.wordaholic_be.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public RoomDto createRoom(RoomCreateDto roomCreateDto) {
        Room room = new Room();
        room.setRoomName(roomCreateDto.getRoomName());
        room.setMaxPlayers(roomCreateDto.getMaxPlayers());
        room.setGameInProgress(false);
        room.setTimeLimit(roomCreateDto.getTimeLimit());
        roomRepository.save(room);
        return new RoomDto(room);
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    @Override
    public RoomDto addPlayerToRoom(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is already in another room
        if (!user.getRooms().isEmpty()) {  // Assuming getRooms() returns the list of rooms the user is in
            throw new RuntimeException(user.getUsername() + " is already in another room.");
        }

        // Check if the user is already a player in this room
        if (room.getPlayers().contains(user)) {
            throw new RuntimeException(user.getUsername() + " is already a player in this room.");
        }

        room.getPlayers().add(user);
        user.getRooms().add(room); // Ensure to add the room to the user's list of rooms
        roomRepository.save(room);
        userRepository.save(user); // Save the user as well
        return new RoomDto(room);
    }

    @Override
    public RoomDto removePlayerFromRoom(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (room.getPlayers().remove(user)) {
            user.getRooms().remove(room); // Remove room from user's list
            roomRepository.save(room);
            userRepository.save(user); // Save updated user
        } else {
            throw new RuntimeException(user.getUsername() + " is not a player in room: " + room.getRoomName());
        }
        return new RoomDto(room);
    }

    @Override
    public RoomDto changeGameStatus(Long roomId, boolean isPlaying) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setGameInProgress(isPlaying);
        roomRepository.save(room);
        return new RoomDto(room);
    }
}
