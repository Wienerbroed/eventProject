package org.example.eventproject.repositories;

import org.example.eventproject.models.Role;
import org.example.eventproject.models.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoginRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public LoginRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int registerUser(UserLogin userLogin) {
        String sql = "INSERT INTO login (username, password, email, role) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, userLogin.getUsername(), userLogin.getPassword(), userLogin.getEmail(), userLogin.getRole().name());
    }

    public boolean isValidUser(String username, String password) {
        String sql = "SELECT COUNT(*) FROM login WHERE username = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        return count != null && count > 0;
    }

    public boolean delegateRole(String adminUsername, String targetUsername, Role newRole) {
        String updateRoleQuery = "UPDATE login SET role = ? WHERE username = ?";
        int rowsAffected = jdbcTemplate.update(updateRoleQuery, newRole.name(), targetUsername);
        return rowsAffected > 0;
    }

    public Role getUserRole(String username) {
        String query = "SELECT role FROM login WHERE username = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{username}, Role.class);
    }

    public boolean existsByUsername(String username) {
        String query = "SELECT COUNT(*) FROM login WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, username);
        return count != null && count > 0;
    }

    public UserLogin findByUsername(String username) {
        String query = "SELECT * FROM login WHERE username = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{username}, (rs, rowNum) ->
                new UserLogin(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        Role.valueOf(rs.getString("role"))
                )
        );
    }

    public void updateUserRole(UserLogin user) {
        String sql = "UPDATE login SET role = ? WHERE username = ?";
        jdbcTemplate.update(sql, user.getRole().name(), user.getUsername());
    }

    public List<UserLogin> findAllUsers() {
        String query = "SELECT * FROM login";
        return jdbcTemplate.query(query, (rs, rowNum) ->
                new UserLogin(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        Role.valueOf(rs.getString("role"))
                )
        );
    }
}
