package org.example.gabartischolarsqbbe.service;

import org.example.gabartischolarsqbbe.entity.User;
import org.example.gabartischolarsqbbe.payload.SignupRequest;

import java.util.Optional;

public interface UserService {
    User registerUser(SignupRequest signupRequest);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    User saveUser(User user);
}
