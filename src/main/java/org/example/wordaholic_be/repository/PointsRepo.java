package org.example.wordaholic_be.repository;

import org.example.wordaholic_be.entity.Points;
import org.example.wordaholic_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointsRepo extends JpaRepository<Points, Long> {
    Optional<Points> findByUser(User user);
}
