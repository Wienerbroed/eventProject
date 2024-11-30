package org.example.eventproject.services;


import org.example.eventproject.models.UserLogin;
import org.example.eventproject.repositories.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {



    @Autowired
    private LoginRepo loginRepo;


    public UserLogin registerUser(UserLogin userLogin) {
        loginRepo.registerUser(userLogin);
        return userLogin;
    }


    public boolean isValidUser(String username, String password) {
        UserLogin user = loginRepo.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean existsByUsername(String username) {
        return loginRepo.existsByUsername(username);
    }







}
