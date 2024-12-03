package org.example.eventproject.services;


import org.example.eventproject.models.Role;
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

    // Method to delegate roles, only allowing admins or superadmins to perform the action
    public boolean delegateRole(String adminUsername, String targetUsername, Role newRole) {
        // Fetch the admin user to ensure they have permission
        UserLogin adminUser = loginRepo.findByUsername(adminUsername);

        // Check if the admin has sufficient privileges (Admin or SuperAdmin)
        if (adminUser != null && (adminUser.getRole() == Role.ADMIN || adminUser.getRole() == Role.SUPERADMIN)) {
            // Update the target user's role
            return loginRepo.updateUserRole(targetUsername, newRole) > 0; // Returns true if successful
        }

        // If admin doesn't have permission, return false
        return false;
    }


    public boolean isValidUser(String username, String password) {
        UserLogin user = loginRepo.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean existsByUsername(String username) {
        return loginRepo.existsByUsername(username);
    }







}
