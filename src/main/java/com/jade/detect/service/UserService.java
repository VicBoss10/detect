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

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByName(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(String id, User partialUser) {
        return userRepository.findById(id).map(existingUser -> {
            // Actualizar los campos que no sean nulos en el UserDTO
            if (partialUser.getUsername() != null) {
                existingUser.setUsername(partialUser.getUsername());
            }
            if (partialUser.getEmail() != null) {
                existingUser.setEmail(partialUser.getEmail());
            }
            if (partialUser.getPassword() != null) {
                existingUser.setPassword(partialUser.getPassword());  // Se actualiza la contraseña
            }
            if (partialUser.getRole() != null) {
                existingUser.setRole(partialUser.getRole());  // Se actualiza el rol (puede ser 'USER' o 'ADMIN')
            }
            return userRepository.save(existingUser);  // Guardamos los cambios en la base de datos
        });
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
