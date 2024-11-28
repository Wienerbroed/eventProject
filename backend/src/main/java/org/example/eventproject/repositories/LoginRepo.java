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
        String insertQuery = "INSERT INTO login (username, password, email, role) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertQuery, username, password, email, role);
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

    public Role getUserRole(String username) {
        String query = "SELECT role FROM login WHERE username = ?";
        return Role.valueOf(jdbcTemplate.queryForObject(query, String.class, username));
    }

    public void logRoleChange(String adminUsername, String targetUsername, Role oldRole, Role newRole) {
        String query = "INSERT INTO role_changes (admin, target_user, old_role, new_role, timestamp) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        jdbcTemplate.update(query, adminUsername, targetUsername, oldRole.name(), newRole.name());
    }
}