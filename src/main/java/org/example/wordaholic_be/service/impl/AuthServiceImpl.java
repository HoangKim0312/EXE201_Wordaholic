package org.example.wordaholic_be.service.impl;

import org.example.wordaholic_be.converter.UserConverter;
import org.example.wordaholic_be.dto.request.LoginRequest;
import org.example.wordaholic_be.dto.request.SignUpRequest;
import org.example.wordaholic_be.dto.response.JwtAuthenticationResponse;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.repository.UserRepository;
import org.example.wordaholic_be.security.JwtTokenProvider;
import org.example.wordaholic_be.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserConverter userConverter;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider tokenProvider,
                           UserConverter userConverter) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userConverter = userConverter;
    }

    @Override
    public JwtAuthenticationResponse signin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(token);
    }

    @Override
    public String signup(SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())){
            return "Username is already taken!";
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return "Email is already taken!";
        }

        // Convert DTO to Entity using ModelMapper
        User user = userConverter.convertToEntity(signUpRequest);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.generateVerificationCode();
        userRepository.save(user);

        return  "User registered successfully";
    }
}