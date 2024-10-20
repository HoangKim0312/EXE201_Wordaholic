package org.example.wordaholic_be.service.impl;

import org.example.wordaholic_be.entity.Points;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.repository.PointsRepo;
import org.example.wordaholic_be.service.PointsService;
import org.springframework.stereotype.Service;

@Service
public class PointsServiceImpl implements PointsService {

    private final PointsRepo pointsRepository;

    public PointsServiceImpl(PointsRepo pointsRepository) {
        this.pointsRepository = pointsRepository;
    }
    @Override
    public Points getPointsByUser(User user) {
        return pointsRepository.findByUser(user)
                .orElseGet(() -> {
                    Points newPoints = new Points();
                    newPoints.setUser(user);
                    newPoints.setTotalPoints(0);
                    return pointsRepository.save(newPoints);
                });
    }

    @Override
    public void saveOrUpdatePoints(Points points) {
        pointsRepository.save(points);
    }

}
