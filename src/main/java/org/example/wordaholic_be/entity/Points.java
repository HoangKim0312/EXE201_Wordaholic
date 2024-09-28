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
}