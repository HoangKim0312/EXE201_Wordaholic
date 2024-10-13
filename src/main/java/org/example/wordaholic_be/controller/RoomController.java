package org.example.wordaholic_be.controller;

import org.example.wordaholic_be.dto.ErrorResponseDto;
import org.example.wordaholic_be.dto.RoomCreateDto;
import org.example.wordaholic_be.dto.RoomDto;
import org.example.wordaholic_be.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;


    @PostMapping("/create")
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomCreateDto roomCreateDto) {
        return ResponseEntity.ok(roomService.createRoom(roomCreateDto));
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join-room/{roomId}/players/{userId}")
    public ResponseEntity<?> addPlayerToRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        try {
            RoomDto roomDto = roomService.addPlayerToRoom(roomId, userId);
            return ResponseEntity.ok(roomDto); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage())); // 400 Bad Request with error message
        }
    }

    @DeleteMapping("/exit-room/{roomId}/players/{userId}")
    public ResponseEntity<RoomDto> removePlayerFromRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        return ResponseEntity.ok(roomService.removePlayerFromRoom(roomId, userId));
    }

    @PutMapping("/{roomId}/status")
    public ResponseEntity<RoomDto> changeGameStatus(@PathVariable Long roomId, @RequestParam boolean isPlaying) {
        return ResponseEntity.ok(roomService.changeGameStatus(roomId, isPlaying));
    }
}
