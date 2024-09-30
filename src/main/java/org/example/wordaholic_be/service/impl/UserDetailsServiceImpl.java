package org.example.wordaholic_be.service.impl;

import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String authority = user.getUsername().equals("admin") ? "ADMIN" : "USER";

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Use email for UserDetails
                user.getPassword(),
                Collections.singletonList(grantedAuthority)
        );
    }
}