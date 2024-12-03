package org.example.eventproject.controllers;

import org.example.eventproject.models.Events;
import org.example.eventproject.models.Role;
import org.example.eventproject.models.UserLogin;
import org.example.eventproject.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping("/register")
    public ResponseEntity<UserLogin> registerUser(@RequestBody UserLogin userLogin) {
        UserLogin createdUser = loginService.registerUser(userLogin);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLogin userLogin) {
        if (loginService.isValidUser(userLogin.getUsername(), userLogin.getPassword())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/delegate-role")
    public ResponseEntity<String> delegateRole(
            @RequestParam String adminUsername,
            @RequestParam String targetUsername,
            @RequestParam Role newRole) {

        boolean success = loginService.delegateRole(adminUsername, targetUsername, newRole);

        if (success) {
            return ResponseEntity.ok("Role updated successfully.");
        } else {
            return ResponseEntity.status(403).body("Insufficient permissions or user not found.");
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "loginAndRegisterPage";
    }

    public boolean isValidUser(String username, String password) {
        return loginService.isValidUser(username, password);
    }

    public boolean existsByUsername(String username) {
        return loginService.existsByUsername(username);
    }
}