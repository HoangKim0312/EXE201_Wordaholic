package org.example.wordaholic_be.controller;

import org.example.wordaholic_be.dto.CreateRoomDto;
import org.example.wordaholic_be.dto.RoomDto;
import org.example.wordaholic_be.dto.UpdateRoomStatusDto;
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
    public ResponseEntity<RoomDto> createRoom(@RequestBody CreateRoomDto roomDto) {
        return ResponseEntity.ok(roomService.createRoom(roomDto));
    }

    @DeleteMapping("/remove/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roomId}/add-player/{email}")
    public ResponseEntity<RoomDto> addPlayerToRoom(@PathVariable String roomId, @PathVariable String email) {
        return ResponseEntity.ok(roomService.addPlayerToRoom(roomId, email));
    }

    @PostMapping("/{roomId}/remove-player/{email}")
    public ResponseEntity<RoomDto> removePlayerFromRoom(@PathVariable String roomId, @PathVariable String email) {
        return ResponseEntity.ok(roomService.removePlayerFromRoom(roomId, email));
    }

    @PatchMapping("/{roomId}/status")
    public ResponseEntity<RoomDto> changeRoomStatus(@PathVariable String roomId, @RequestBody UpdateRoomStatusDto statusDto) {
        return ResponseEntity.ok(roomService.changeRoomStatus(roomId, statusDto));
    }
}
