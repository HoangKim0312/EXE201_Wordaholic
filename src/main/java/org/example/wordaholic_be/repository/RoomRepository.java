package org.example.wordaholic_be.repository;

import org.example.wordaholic_be.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}