package org.example.wordaholic_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(nullable = false)
    private String roomName;

    @ManyToMany
    @JoinTable(
            name = "room_users", // Use a single join table name
            joinColumns = @JoinColumn(name = "room_id"), // Foreign key in the join table
            inverseJoinColumns = @JoinColumn(name = "user_id") // Foreign key for the User entity
    )
    private List<User> players = new ArrayList<>(); // No need to declare another List<User> users

    @Column(nullable = false)
    private int maxPlayers;

    @Column(nullable = false)
    private int currentPlayerIndex;

    @Column(nullable = false)
    private boolean gameInProgress;

    @ManyToOne
    @JoinColumn(name = "current_word_id")
    private Dictionary currentWord;

    @Column(nullable = false)
    private int timeLimit;

    @ManyToMany
    @JoinTable(
            name = "room_used_words", // Ensure you have a distinct name for this join table
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "dictionary_id") // Assuming Dictionary has an ID
    )
    private List<Dictionary> usedWords = new ArrayList<>(); // Initialized here

    // Method to check if the room can accept a new player
    public boolean canJoin(User user) {
        return players.size() < maxPlayers && !players.contains(user);
    }
}
