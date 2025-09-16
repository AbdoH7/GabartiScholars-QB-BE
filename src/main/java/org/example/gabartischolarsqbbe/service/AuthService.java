package org.example.gabartischolarsqbbe.service;

import org.example.gabartischolarsqbbe.entity.User;
import org.example.gabartischolarsqbbe.payload.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(SignupRequest signupRequest) {
        return userService.registerUser(signupRequest);
    }

    public User authenticateUser(String username, String password) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        User user = userOptional.get();
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        return user;
    }
}
