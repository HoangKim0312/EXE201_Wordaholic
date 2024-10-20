package org.example.wordaholic_be.service;

import org.example.wordaholic_be.entity.Points;
import org.example.wordaholic_be.entity.User;

public interface PointsService {
    Points getPointsByUser(User user); // Retrieves points for a given user
    void saveOrUpdatePoints(Points points);
}
