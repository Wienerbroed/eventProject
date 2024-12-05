package org.example.eventproject.controllers;

import org.example.eventproject.models.Events;
import org.example.eventproject.models.UserLogin;
import org.example.eventproject.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/loginAndRegister")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "loginAndRegisterPage";
    }


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


}