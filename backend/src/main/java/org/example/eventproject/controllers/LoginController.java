package org.example.eventproject.controllers;

import org.example.eventproject.models.Role;
import org.example.eventproject.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(String username, String password, String email, String role) {
        loginService.registerUser(username, password, email, role);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(String username, String password) {
        if (loginService.isValidUser(username, password)) {
            return "redirect:/home";
        } else {
            return "redirect:/login?error";
        }
    }

    public boolean isValidUser(String username, String password) {
        return loginService.isValidUser(username, password);
    }

    public boolean existsByUsername(String username) {
        return loginService.existsByUsername(username);
    }

    @PostMapping("/admin/delegate-role")
    public String delegateRole(@RequestParam String username, @RequestParam Role role) {
        // Only allow ADMINs to change roles
        // You can implement an additional check here for the current user's role
        // if they are an ADMIN, or use a security context to check
        loginService.changeUserRole(username, role);
        return "redirect:/admin/dashboard";
    }

}