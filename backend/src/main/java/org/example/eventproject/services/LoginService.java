package org.example.eventproject.services;

import org.example.eventproject.repositories.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginRepo loginRepo;

    public void registerUser(String username, String password, String email) {
        // Basic validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }

        loginRepo.registerUser(username, password, email);
    }

    public boolean isValidUser(String username, String password) {
        return loginRepo.isValidUser(username, password);
    }

    public boolean existsByUsername(String username) {
        return loginRepo.existsByUsername(username);
    }
}