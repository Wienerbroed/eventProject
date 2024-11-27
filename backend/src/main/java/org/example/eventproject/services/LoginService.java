package org.example.eventproject.services;


import org.example.eventproject.models.Role;
import org.example.eventproject.repositories.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {



    @Autowired
    private LoginRepo loginRepo;


    public void registerUser(String username, String password, String email, String role) {
        loginRepo.registerUser(username, password, email, role);
    }

    public void loginUser(String username, String password) {
        loginRepo.loginUser(username, password);
    }

    public boolean isValidUser(String username, String password) {
        return loginRepo.isValidUser(username, password);
    }

    public boolean existsByUsername(String username) {
        return loginRepo.existsByUsername(username);
    }

    // method to change the user's role
    public void changeUserRole(String username, Role newRole) {
        loginRepo.updateUserRole(username, newRole);
    }






}
