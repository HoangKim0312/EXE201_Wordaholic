package org.example.wordaholic_be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "points")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Points {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointsId;

    @Column(nullable = false)
    private int totalPoints;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int gamePoints;

    public void addPoints(int points) {
        this.totalPoints += points;
    }

    public void subtractPoints(int points) {
        if (this.totalPoints - points < 0) {
            this.totalPoints = 0; // Ensure totalPoints does not go below 0
        } else {
            this.totalPoints -= points; // Subtract points if it does not go negative
        }
    }
}