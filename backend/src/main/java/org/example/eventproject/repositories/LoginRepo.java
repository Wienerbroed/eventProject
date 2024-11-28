package org.example.eventproject.repositories;

import org.example.eventproject.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepo {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public LoginRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void registerUser(String username, String password, String email, String role) {
        long userId = generateUserId();
        String insertQuery = "INSERT INTO login (id, username, password, email, role) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertQuery, userId, username, password, email, role);
    }

    public void loginUser(String username, String password) {
        String insertQuery = "INSERT INTO login (username, password) VALUES (?, ?)";
        jdbcTemplate.update(insertQuery, username, password);
    }


    public boolean isValidUser(String username, String password) {
        String sql = "SELECT COUNT(*) FROM login WHERE username = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        return count != null && count > 0;
    }


    public boolean existsByUsername(String username) {
        String query = "SELECT COUNT(*) FROM login WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, username);
        return count != null && count > 0;
    }

    public long generateUserId() {
        long id;
        do {
            id = (long) (Math.random() * 1_000_000_000L); // Generate a random 9-digit number
        } while (id % 10 == 0 || id % 10 == 1); // Ensure it doesn't end with 0 or 1
        return id;
    }

    public void changeRole(Long userId, Role newRole) {
        // Determine the suffix based on the role
        String roleIdSuffix = newRole == Role.ADMIN ? "0" : newRole == Role.AFVIKLER ? "1" : null;

        if (roleIdSuffix != null) {
            // Remove the last digit if it's 0 or 1, then append the new suffix
            String newIdString = userId.toString().replaceAll("[01]$", "") + roleIdSuffix;
            long newId = Long.parseLong(newIdString);

            // Update the user's role and ID in the database
            String updateQuery = "UPDATE login SET id = ?, role = ? WHERE id = ?";
            jdbcTemplate.update(updateQuery, newId, newRole.toString(), userId);
        } else {
            // If the role doesn't require a suffix, just update the role
            String updateRoleQuery = "UPDATE login SET role = ? WHERE id = ?";
            jdbcTemplate.update(updateRoleQuery, newRole.toString(), userId);
        }
    }

}