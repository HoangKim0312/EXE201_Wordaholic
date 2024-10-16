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

        @ManyToMany(mappedBy = "players")
        private List<Room> rooms = new ArrayList<>();

        public boolean joinRoom(Room room) {
            if (rooms.isEmpty() && !room.getPlayers().contains(this)) {
                rooms.add(room);
                room.getPlayers().add(this);
                return true; // Successfully joined the room
            }
            return false; // Failed to join (already in a room or already in the room)
        }

        // Method to leave a room
        public void leaveRoom(Room room) {
            if (rooms.remove(room)) {
                room.getPlayers().remove(this);
            }
        }
    }