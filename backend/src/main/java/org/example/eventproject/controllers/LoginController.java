package org.example.eventproject.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.eventproject.models.Role;
import org.example.eventproject.models.UserLogin;
import org.example.eventproject.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

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
    public ResponseEntity<String> loginUser(@RequestBody UserLogin userLogin, HttpSession session) {
        if (loginService.isValidUser(userLogin.getUsername(), userLogin.getPassword())) {
            // Store user details in session
            session.setAttribute("username", userLogin.getUsername());
            session.setAttribute("role", loginService.getUserRole(userLogin.getUsername()));
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @GetMapping("/admin/panel")
    public ResponseEntity<?> showAdminPanel(HttpSession session) {
        Role role = (Role) session.getAttribute("role");

        if (role == Role.ADMIN || role == Role.SUPERADMIN) {
            List<UserLogin> users = loginService.getAllUsers();
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(403).body("Access denied: insufficient permissions");
        }
    }

    @PostMapping("/admin/delegate-role")
    public ResponseEntity<String> updateUserRole(
            @RequestParam String targetUsername,
            @RequestParam Role newRole,
            HttpSession session) {

        String adminUsername = (String) session.getAttribute("username");
        Role adminRole = (Role) session.getAttribute("role");

        if (adminRole == Role.ADMIN || adminRole == Role.SUPERADMIN) {
            boolean success = loginService.delegateRole(adminUsername, targetUsername, newRole);
            if (success) {
                return ResponseEntity.ok("Role updated successfully.");
            } else {
                return ResponseEntity.status(404).body("User not found or update failed.");
            }
        } else {
            return ResponseEntity.status(403).body("Access denied: insufficient permissions.");
        }
    }

    @GetMapping("/admin/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(Arrays.asList(Role.values()));
    }
}