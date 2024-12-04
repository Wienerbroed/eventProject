package org.example.eventproject.services;


import org.example.eventproject.models.Role;
import org.example.eventproject.models.UserLogin;
import org.example.eventproject.repositories.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {



    @Autowired
    private LoginRepo loginRepo;


    public UserLogin registerUser(UserLogin userLogin) {
        // Ensure role is set
        if (userLogin.getRole() == null) {
            userLogin.setRole(Role.USER);
        }
        loginRepo.registerUser(userLogin);
        return userLogin;
    }

    public boolean isValidUser(String username, String password) {
        return loginRepo.isValidUser(username, password);
    }

    public boolean existsByUsername(String username) {
        return loginRepo.existsByUsername(username);
    }

    public List<UserLogin> findAllUsers() {
        return loginRepo.findAllUsers();
    }

    public boolean assignUserRole(UserLogin userLogin) {
        if (!loginRepo.existsByUsername(userLogin.getUsername())) {
            return false;
        }
        loginRepo.updateUser(userLogin);
        return true;
    }

}