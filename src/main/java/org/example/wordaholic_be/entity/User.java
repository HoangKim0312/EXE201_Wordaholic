package org.example.wordaholic_be.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID; // For generating verification tokens

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
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

    @Column(columnDefinition = "boolean default false") // Default to not enabled
    private boolean enabled;

    @Column
    private String verificationCode;

    @Column
    private LocalDateTime verificationCodeExpiration;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Currency currency;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Points points;

    @ManyToMany(mappedBy = "users")
    private List<Leaderboard> leaderboards;

    // Method to generate and set a new verification code
    public void generateVerificationCode() {
        this.verificationCode = UUID.randomUUID().toString();
        this.verificationCodeExpiration = LocalDateTime.now().plusHours(24); // Token valid for 24 hours
    }
}