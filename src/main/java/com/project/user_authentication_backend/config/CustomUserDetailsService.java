package com.project.user_authentication_backend.config;


import com.project.user_authentication_backend.dao.UserRepository;
import com.project.user_authentication_backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Assume you have a UserRepository to access user data

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // Existing method to load by username
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities(String.valueOf(user.getRole()))
                .build();
    }

    // New method to load by userId
    public UserDetails loadUserById(int userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities(String.valueOf(user.getRole())) // Ensure this matches your roles/authorities
                .build();
    }


}