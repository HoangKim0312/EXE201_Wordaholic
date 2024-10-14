package org.example.wordaholic_be.service;

import org.example.wordaholic_be.dto.LoginDto;
import org.example.wordaholic_be.dto.RegisterDto;
import org.example.wordaholic_be.dto.UserDto;
import org.example.wordaholic_be.entity.User;

import java.util.List;

public interface UserService {

    String register(RegisterDto registerDto);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String login(LoginDto loginDto);

    String forgotPassword(String email);

    String setPassword(String email, String newPassword);

    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    User findUserByEmail(String email);
}