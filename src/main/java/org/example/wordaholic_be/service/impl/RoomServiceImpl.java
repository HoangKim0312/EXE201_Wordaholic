package org.example.wordaholic_be.service.impl;

import org.example.wordaholic_be.dto.CreateRoomDto;
import org.example.wordaholic_be.dto.RoomDto;
import org.example.wordaholic_be.dto.UpdateRoomStatusDto;
import org.example.wordaholic_be.entity.Room;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.repository.RoomRepository;
import org.example.wordaholic_be.repository.UserRepository;
import org.example.wordaholic_be.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public RoomDto createRoom(CreateRoomDto roomDto) {
        Room room = new Room();
        room.setRoomId(Room.generateRandomRoomId());
        room.setRoomName(roomDto.getRoomName());
        room.setMaxPlayers(roomDto.getMaxPlayers());
        room.setTimeLimit(roomDto.getTimeLimit());
        room.setCurrentPlayerIndex(0);
        room.setGameInProgress(false);
        roomRepository.save(room);

        return mapToRoomDto(room);
    }

    @Override
    public void deleteRoom(String roomId) {
        roomRepository.deleteById(roomId);
    }

    @Override
    public RoomDto addPlayerToRoom(String roomId, String userEmail) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the user is already in another room
        if (!user.getRooms().isEmpty()) {
            throw new RuntimeException("User is already in another room");
        }

        // If the user can join, proceed with adding them to the room
        if (room.canJoin(user)) {
            user.joinRoom(room);
            roomRepository.save(room);
        } else {
            throw new RuntimeException("Room is full or user already in the room");
        }

        return mapToRoomDto(room);
    }

    @Override
    public RoomDto removePlayerFromRoom(String roomId, String userEmail) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        user.leaveRoom(room);
        roomRepository.save(room);

        return mapToRoomDto(room);
    }

    @Override
    public RoomDto changeRoomStatus(String roomId, UpdateRoomStatusDto statusDto) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.setGameInProgress(statusDto.isGameInProgress());
        roomRepository.save(room);

        return mapToRoomDto(room);
    }

    private RoomDto mapToRoomDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(room.getRoomId());
        roomDto.setRoomName(room.getRoomName());
        roomDto.setMaxPlayers(room.getMaxPlayers());
        roomDto.setGameInProgress(room.isGameInProgress());
        roomDto.setCurrentPlayerIndex(room.getCurrentPlayerIndex());
        roomDto.setTimeLimit(room.getTimeLimit());
        roomDto.setPlayersEmails(room.getPlayers().stream().map(User::getEmail).collect(Collectors.toList()));
        return roomDto;
    }
}
