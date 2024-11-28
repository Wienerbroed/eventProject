package org.example.eventproject.controllers;

import org.example.eventproject.models.Events;
import org.example.eventproject.models.UserLogin;
import org.example.eventproject.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;


    @GetMapping("/loginAndRegisterPage")
    public String showLoginAndRegisterPage() {
        return "loginAndRegisterPage";
    }

    /*@PostMapping("/register")
    public String registerUser(String username, String password, String email) {
        loginService.registerUser(username, password, email);
        return "redirect:/loginAndRegisterPage"; // Redirects back to the login and register page
    }*/

    @PostMapping("/register")
    public ResponseEntity<UserLogin> registerUser(@RequestBody UserLogin userLogin) {
        UserLogin createdUser = loginService.registerUser(userLogin);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public String loginUser(String username, String password) {
        if (loginService.isValidUser(username, password)) {
            return "redirect:/home"; // Redirects to the home page if login is successful
        } else {
            return "redirect:/loginAndRegisterPage?error=true"; // Redirects back with an error flag
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }


    public boolean isValidUser(String username, String password) {
        return loginService.isValidUser(username, password);
    }

    public boolean existsByUsername(String username) {
        return loginService.existsByUsername(username);
    }
}