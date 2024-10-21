package org.example.wordaholic_be.service.impl;

import jakarta.mail.MessagingException;
import org.example.wordaholic_be.dto.LoginDto;
import org.example.wordaholic_be.dto.RegisterDto;
import org.example.wordaholic_be.dto.UserDto;
import org.example.wordaholic_be.entity.Currency;
import org.example.wordaholic_be.entity.Points;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.repository.CurrencyRepo;
import org.example.wordaholic_be.repository.PointsRepo;
import org.example.wordaholic_be.repository.UserRepository;
import org.example.wordaholic_be.service.UserService;
import org.example.wordaholic_be.util.EmailUtil;
import org.example.wordaholic_be.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyRepo currencyRepository;

    @Autowired
    private PointsRepo pointsRepository;

    @Override
    public String register(RegisterDto registerDto) {
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send OTP. Please try again.");
        }

        // Create and save the user
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        user.setEnabled(false); // Set default enabled value
        user.setActive(false); // Set default active value
        user.setCoins(50); // Set initial coins to 50 instead of 0
        userRepository.save(user);

        // Create and save currency for the new user
        Currency currency = new Currency();
        currency.setTotalCurrency(50);  // Assign 50 coins
        currency.setCurrencyName("Coins"); // Set currency name
        currency.setUser(user); // Link the currency to the newly created user
        currencyRepository.save(currency); // Save the currency entry

        // Set the currency in the user
        user.setCurrency(currency); // Set the currency in the user
        userRepository.save(user); // Make sure to save user again to update the link

        // Initialize points to 0
        Points points = new Points();
        points.setTotalPoints(0); // Initialize points to 0
        points.setUser(user); // Link the points to the newly created user
        pointsRepository.save(points); // Save the points entry

        return "User registration successful. An OTP has been sent to your email.";
    }

    @Override
    public String verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setActive(true);
            userRepository.save(user);
            return "OTP verified you can login";
        }
        return "Please regenerate otp and try again";
    }

    @Override
    public String regenerateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        return "Email sent... please verify account within 1 minute";
    }

    @Override
    public String login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(
                        () -> new RuntimeException("User not found with this email: " + loginDto.getEmail()));
        if (!loginDto.getPassword().equals(user.getPassword())) {
            return "Password is incorrect";
        } else if (!user.isActive()) {
            return "your account is not verified";
        }
        return "Login successful";
    }
    @Override
    public String forgotPassword(String email) {
        User user = findUserByEmail(email);

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiration(LocalDateTime.now().plusHours(1)); // Example: 1-hour expiration
        userRepository.save(user);

        String resetLink = "http://localhost:8080/api/users/reset-password?token=" + token;
        try {
            emailUtil.sendPasswordResetEmail(email, resetLink);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return "Password reset email sent! (Check your inbox/spam)";
    }

    @Override
    public String resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (user.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        user.setPassword(newPassword);
        user.setResetToken(null);
        user.setResetTokenExpiration(null);
        userRepository.save(user);

        return "Password reset successful!";
    }


    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setEnabled(userDto.isEnabled());
        user.setActive(userDto.isActive());

        // Update currency if provided in the DTO
        if (userDto.getTotalCurrency() != null) {
            user.getCurrency().setTotalCurrency(userDto.getTotalCurrency());
        }

        // Update points if provided in the DTO
        if (userDto.getTotalPoints() != null) {
            user.getPoints().setTotalPoints(userDto.getTotalPoints());
        }

        userRepository.save(user);
        return mapToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                user.isActive(),
                user.getPoints() != null ? user.getPoints().getTotalPoints() : null,  // Map points if they exist
                user.getCurrency() != null ? user.getCurrency().getTotalCurrency() : null  // Map currency if it exists
        );
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
    }
    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public void updateUserCoins(Long userId, int coinsToAdd) {
        User user = findUserById(userId);
        if (user != null) {
            user.setCoins(user.getCoins() + coinsToAdd);
            userRepository.save(user);
        }
    }
}