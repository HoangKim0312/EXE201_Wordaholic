package org.example.wordaholic_be.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "leaderboards")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaderboardId;

    @ManyToMany
    @JoinTable(
            name = "user_leaderboard",
            joinColumns = @JoinColumn(name = "leaderboard_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;
}