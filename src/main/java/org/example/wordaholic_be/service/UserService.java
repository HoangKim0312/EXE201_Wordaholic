package org.example.wordaholic_be.service;

import org.example.wordaholic_be.dto.LoginDto;
import org.example.wordaholic_be.dto.RegisterDto;

public interface UserService {

    String register(RegisterDto registerDto);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String login(LoginDto loginDto);

    String forgotPassword(String email);

    String setPassword(String email, String newPassword);
}