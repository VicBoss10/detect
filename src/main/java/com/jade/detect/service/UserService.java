package com.jade.detect.service;

import com.jade.detect.model.User;
import com.jade.detect.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByName(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, User partialUser) {
        return userRepository.findById(id).map(existingUser -> {
            if (partialUser.getUsername() != null) {
                existingUser.setUsername(partialUser.getUsername());
            }
            if (partialUser.getEmail() != null) {
                existingUser.setEmail(partialUser.getEmail());
            }
            return userRepository.save(existingUser);
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
