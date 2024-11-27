package org.example.eventproject.controllers;

import org.example.eventproject.models.UserLogin;
import org.example.eventproject.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "redirect:/register.html";
    }

    @PostMapping("/register")
    public String registerUser(String username, String password, String email) {
        loginService.registerUser(username, password, email);
        return "redirect:/register";
    }


   /* @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserLogin userLogin){
        loginService.registerUser(userLogin.getUsername(), userLogin.getPassword(), userLogin.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
    } */



}