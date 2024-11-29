package org.example.eventproject.controllers;

import org.example.eventproject.models.Role;
import org.example.eventproject.models.UserLogin;
import org.example.eventproject.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.AccessDeniedException;

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
    public String registerUser(String username, String password, String email, String role) {
        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(password);
        loginService.registerUser(username, encodedPassword, email, role);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
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
}