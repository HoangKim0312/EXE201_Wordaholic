package org.example.wordaholic_be.repository;

import org.example.wordaholic_be.entity.Room;
import org.example.wordaholic_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String> {
}