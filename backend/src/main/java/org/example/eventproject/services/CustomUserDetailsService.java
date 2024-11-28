package org.example.eventproject.services;

import org.example.eventproject.models.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve the full UserLogin object
        UserLogin userLogin = loginService.getUserByUsername(username);

        // If user not found, throw exception
        if (userLogin == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Create authorities based on the user's role
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + userLogin.getRole().name())
        );

        // Return Spring Security User object
        return new User(
                userLogin.getUsername(),
                userLogin.getPassword(),
                authorities
        );
    }
}