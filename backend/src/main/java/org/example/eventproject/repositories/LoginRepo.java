package org.example.eventproject.repositories;

import org.example.eventproject.models.Role;
import org.example.eventproject.models.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LoginRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to convert database row to UserLogin object
    private RowMapper<UserLogin> userLoginRowMapper = (rs, rowNum) -> {
        UserLogin user = new UserLogin();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(Role.valueOf(rs.getString("role")));
        return user;
    };

    public UserLogin findByUsername(String username) {
        try {
            String query = "SELECT id, username, password, email, role FROM login WHERE username = ?";
            return jdbcTemplate.queryForObject(query, userLoginRowMapper, username);
        } catch (Exception e) {
            return null;
        }
    }

    public LoginRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerUser(String username, String encodedPassword, String email, String role) {
        String insertQuery = "INSERT INTO login (username, password, email, role) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertQuery, username, encodedPassword, email, role);
    }

    public void loginUser(String username, String password) {
        // This method might need to be revised or removed with Spring Security
        String insertQuery = "INSERT INTO login (username, password) VALUES (?, ?)";
        jdbcTemplate.update(insertQuery, username, password);
    }

    public String getEncodedPasswordByUsername(String username) {
        try {
            String query = "SELECT password FROM login WHERE username = ?";
            return jdbcTemplate.queryForObject(query, String.class, username);
        } catch (Exception e) {
            return null;
        }
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

    public void changeUserRole(String username, Role newRole) {
        String query = "UPDATE login SET role = ? WHERE username = ?";
        jdbcTemplate.update(query, newRole.name(), username);
    }
}