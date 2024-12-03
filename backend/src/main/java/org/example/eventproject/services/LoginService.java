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
        loginRepo.registerUser(userLogin);
        return userLogin;
    }

    // Admins or SuperAdmins can assign roles to users
    public boolean delegateRole(String adminUsername, String targetUsername, Role newRole) {
        UserLogin adminUser = loginRepo.findByUsername(adminUsername);
        UserLogin targetUser = loginRepo.findByUsername(targetUsername);

        if (adminUser == null || targetUser == null) {
            throw new IllegalArgumentException("Admin or target user not found");
        }

        // Only Admin or SuperAdmin can assign roles
        if (adminUser.getRole() == Role.ADMIN || adminUser.getRole() == Role.SUPERADMIN) {
            targetUser.assignRole(adminUser, newRole); // Ensures proper validation in UserLogin
            loginRepo.updateUserRole(targetUser); // Persist role change in the database
            return true;
        } else {
            throw new SecurityException("Access denied: insufficient permissions to delegate roles.");
        }
    }

    public boolean isValidUser(String username, String password) {
        UserLogin user = loginRepo.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean existsByUsername(String username) {
        return loginRepo.existsByUsername(username);
    }

    public List<UserLogin> getAllUsers() {
        return loginRepo.findAllUsers();
    }

    public Role getUserRole(String username) {
        return loginRepo.getUserRole(username);
    }
}
