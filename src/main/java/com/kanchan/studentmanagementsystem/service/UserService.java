package com.kanchan.studentmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kanchan.studentmanagementsystem.entity.User;
import com.kanchan.studentmanagementsystem.repository.UserRepository;

@Service
public class UserService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    // Login
    public User login(String username, String password) {

        User user = userRepository.findByUsername(username);

        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    // Register
    public void register(User user) {

        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Total Users
    public long getUserCount() {
            return userRepository.count();
        }
        public User getUserByEmail(String email) {

        return userRepository.findByEmail(email);

    }

    public void resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(
                    encoder.encode(password));
            userRepository.save(user);
        }
    }

}
