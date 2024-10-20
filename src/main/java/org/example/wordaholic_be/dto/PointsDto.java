package org.example.wordaholic_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointsDto {
    private Long pointsId;  // ID of the points entry
    private int totalPoints; // Total points associated with the user
    private Long userId;     // ID of the associated user (optional)
}
