package org.example.wordaholic_be.service;

import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.generateResetToken();
        userRepository.save(user);

        String resetLink = "http://your-frontend-url/reset-password?token=" + user.getResetToken();
        String emailContent = "Click here to reset your password: " + resetLink;
        emailService.sendEmail(email, "Password Reset Request", emailContent);
    }

    public boolean validateResetToken(String token) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        return userOptional.isPresent() && userOptional.get().getResetTokenExpiration().isAfter(LocalDateTime.now());
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        if (validateResetToken(token)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiration(null);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid or expired reset token");
        }
    }
}