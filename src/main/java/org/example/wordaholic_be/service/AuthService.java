package org.example.wordaholic_be.service;

import org.example.wordaholic_be.dto.request.LoginRequest;
import org.example.wordaholic_be.dto.request.SignUpRequest;
import org.example.wordaholic_be.dto.response.JwtAuthenticationResponse;

public interface AuthService {
    JwtAuthenticationResponse signin(LoginRequest loginRequest);
    String signup(SignUpRequest signUpRequest);
}