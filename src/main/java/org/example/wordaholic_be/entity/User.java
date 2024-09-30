package org.example.wordaholic_be.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
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

    @Column
    private String verificationCode;

    @Column
    private LocalDateTime verificationCodeExpiration;

    @Column
    private String resetToken;

    @Column
    private LocalDateTime resetTokenExpiration;

    public void generateVerificationCode() {
        this.verificationCode = UUID.randomUUID().toString();
        this.verificationCodeExpiration = LocalDateTime.now().plusHours(24); // Token valid for 24 hours
    }

    public void generateResetToken() {
        this.resetToken = UUID.randomUUID().toString();
        this.resetTokenExpiration = LocalDateTime.now().plusHours(24); // Token valid for 24 hours
    }
}