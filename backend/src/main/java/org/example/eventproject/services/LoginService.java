package org.example.eventproject.services;

import org.example.eventproject.models.Role;
import org.example.eventproject.models.UserLogin;

import org.example.eventproject.models.UserLogin;
import org.example.eventproject.repositories.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {



    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Method to retrieve full UserLogin object
    public UserLogin getUserByUsername(String username) {
        return loginRepo.findByUsername(username);
    }

    public void registerUser(String username, String password, String email, String role) {
        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(password);
        loginRepo.registerUser(username, encodedPassword, email, role);
    }

    public void loginUser(String username, String password) {
        loginRepo.loginUser(username, password);
    }

    public boolean isValidUser(String username, String password) {
        // Retrieve the stored encoded password and check it
        String storedPassword = loginRepo.getEncodedPasswordByUsername(username);
        return storedPassword != null && passwordEncoder.matches(password, storedPassword);
    }

    public boolean existsByUsername(String username) {
        return loginRepo.existsByUsername(username);
    }

    public boolean isValidRole(Role role) {
        return List.of(Role.USER, Role.AFVIKLER).contains(role); // Admin can delegate these roles only
    }

    public Role getUserRole(String username) {
        return loginRepo.getUserRole(username);
    }

    public void logRoleChange(String adminUsername, String targetUsername, Role oldRole, Role newRole) {
        loginRepo.logRoleChange(adminUsername, targetUsername, oldRole, newRole);
    }

    public void changeUserRole(String username, Role newRole) {
        loginRepo.changeUserRole(username, newRole);
    }
}