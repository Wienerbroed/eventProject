package org.example.eventproject.controllers;

import org.example.eventproject.models.Role;
import org.example.eventproject.models.UserLogin;
import org.example.eventproject.models.Events;
import org.example.eventproject.models.UserLogin;
import org.example.eventproject.models.Role;
import org.example.eventproject.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<UserLogin> registerUser(@RequestBody UserLogin userLogin) {
        UserLogin createdUser = loginService.registerUser(userLogin);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/register")
    public String registerUser(String username, String password, String email, String role) {
        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(password);
        loginService.registerUser(username, encodedPassword, email, role);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLogin userLogin) {
        if (loginService.isValidUser(userLogin.getUsername(), userLogin.getPassword())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/admin/delegate-role")
    public String delegateRole(
            @RequestParam String username,
            @RequestParam Role role,
            Authentication authentication) throws AccessDeniedException {

        // This method is now protected by method-level security in SecurityConfig
        // Prevent self-role modification
        if (username.equals(authentication.getName())) {
            throw new AccessDeniedException("Admins cannot change their own roles.");
        }

        Role currentRole = loginService.getUserRole(username);
        if (currentRole == role) {
            throw new IllegalArgumentException("The user already has this role.");
        }

        if (!loginService.isValidRole(role)) {
            throw new AccessDeniedException("You cannot delegate this role.");
        }

        // Change role and log the action
        loginService.changeUserRole(username, role);
        loginService.logRoleChange(authentication.getName(), username, currentRole, role);

        return "redirect:/admin/dashboard?success=true";
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