    package org.example.wordaholic_be.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;

    @Entity
    @Table(name = "users")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long userId;

        @Column(unique = true, nullable = false)
        private String username;

        @Column(nullable = false)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(columnDefinition = "boolean default false")
        private boolean enabled;

        private boolean active;
        private String otp;
        private LocalDateTime otpGeneratedTime;
        private String resetToken;
        private LocalDateTime resetTokenExpiration;

        @ManyToMany(mappedBy = "players")
        private List<Room> rooms = new ArrayList<>();

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Currency currency;

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Points points;

        @Column(nullable = false)
        private Integer coins = 0;

        public boolean joinRoom(Room room) {
            // If the user is already in a room, prevent them from joining another room
            if (!rooms.isEmpty()) {
                return false; // The user is already in a room
            }

            // Check if the room has space and the user isn't already in this room
            if (room.canJoin(this)) {
                rooms.add(room);
                room.getPlayers().add(this);
                return true; // Successfully joined the room
            }

            return false; // Room is full or the user is already in this room
        }

        // Method to leave a room
        public void leaveRoom(Room room) {
            if (rooms.remove(room)) {
                room.getPlayers().remove(this);
            }
        }
    }