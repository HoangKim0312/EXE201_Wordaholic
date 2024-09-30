package org.example.wordaholic_be.service;

import org.example.wordaholic_be.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService { // Now an interface

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    User findUserByEmail(String email);

    void saveUser(User user);
}