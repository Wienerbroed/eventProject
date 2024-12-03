package org.example.eventproject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "login")
public class UserLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    // Permission logic for assigning roles
    public boolean canAssignRole(UserLogin adminUser) {
        return adminUser.getRole() == Role.ADMIN || adminUser.getRole() == Role.SUPERADMIN;
    }

    public void assignRole(UserLogin adminUser, Role newRole) {
        if (!canAssignRole(adminUser)) {
            throw new SecurityException("Only Admin or SuperAdmin can assign roles");
        }
        this.role = newRole;
    }

    // Constructors, Getters, and Setters
    public UserLogin(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public UserLogin() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}