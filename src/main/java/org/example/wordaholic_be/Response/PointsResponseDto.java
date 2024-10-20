package org.example.wordaholic_be.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointsResponseDto {

        private Long pointsId;
        private int totalPoints;

        public PointsResponseDto(Long pointsId, int totalPoints) {
            this.pointsId = pointsId;
            this.totalPoints = totalPoints;
        }

    }
