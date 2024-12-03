package org.example.eventproject.repositories;

import org.example.eventproject.models.Role;
import org.example.eventproject.models.UserLogin;
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


    /*public void registerUser(String username, String password, String email) {
        String insertQuery = "INSERT INTO login (username, password, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertQuery, username, password, email);
    }*/

    public int registerUser(UserLogin userLogin) {
        String sql = "INSERT INTO login (username, password, email) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, userLogin.getUsername(), userLogin.getPassword(), userLogin.getEmail());
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

    // Update the user's role in the database
    public int updateUserRole(String username, org.example.eventproject.models.Role newRole) {
        String sql = "UPDATE login SET role = ? WHERE username = ?";
        return jdbcTemplate.update(sql, newRole.name(), username); // Update the role in the database
    }

    // Find a user by username
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
}