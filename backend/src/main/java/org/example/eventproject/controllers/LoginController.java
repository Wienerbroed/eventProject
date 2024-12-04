package org.example.eventproject.controllers;

import org.example.eventproject.models.UserLogin;
import org.example.eventproject.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    // Register a user
    @PostMapping("/register")
    public ResponseEntity<UserLogin> registerUser(@RequestBody UserLogin userLogin) {
        UserLogin createdUser = loginService.registerUser(userLogin);
        return ResponseEntity.ok(createdUser);
    }

    // Login a user
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLogin userLogin) {
        if (loginService.isValidUser(userLogin.getUsername(), userLogin.getPassword())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<UserLogin>> getAllUsers() {
        List<UserLogin> users = loginService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // Assign role to a user
    @PostMapping("/assignRole")
    public ResponseEntity<String> assignRole(@RequestBody UserLogin userLogin) {
        if (loginService.assignUserRole(userLogin)) {
            return ResponseEntity.ok("Role assigned successfully");
        } else {
            return ResponseEntity.badRequest().body("Role assignment failed");
        }
    }
}
