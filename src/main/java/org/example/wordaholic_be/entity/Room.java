package org.example.wordaholic_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    private String roomId; // Remove the GeneratedValue

    @Column(nullable = false)
    private String roomName;

    @ManyToMany
    @JoinTable(
            name = "room_users",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players = new ArrayList<>();

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
            name = "room_used_words",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "dictionary_id")
    )
    private List<Dictionary> usedWords = new ArrayList<>();

    // Method to generate a random 5-character string
    public static String generateRandomRoomId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder roomId = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            roomId.append(chars.charAt(random.nextInt(chars.length())));
        }
        return roomId.toString();
    }

    public boolean canJoin(User user) {
        return players.size() < maxPlayers && !players.contains(user);
    }
}
