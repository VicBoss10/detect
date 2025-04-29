package com.jade.detect.controller;

import com.jade.detect.model.UserDTO;
import com.jade.detect.model.User;
import com.jade.detect.repository.IKeyCloakRepository;
import com.jade.detect.service.UserService;
import com.jade.detect.util.UserAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('admin_client_role')")
@Tag(name = "Usuarios", description = "Gestión de usuarios en el sistema")
public class UserController {

    private final UserService userService;
    private final IKeyCloakRepository keyCloakService;

    @Autowired
    public UserController(UserService userService, IKeyCloakRepository keyCloakService) {
        this.userService = userService;
        this.keyCloakService = keyCloakService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios de Keycloak")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<List<UserRepresentation>> getUsers() {
        return ResponseEntity.ok(keyCloakService.findAllUsers());
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Buscar usuario por ID de Keycloak")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<UserRepresentation> getUserById(
            @Parameter(description = "ID del usuario en Keycloak", example = "f3a9322c-bb54-4b15-bd3a-1f0ac896d555")
            @PathVariable String id) {

        try {
            UserRepresentation user = keyCloakService.findUserById(id);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Buscar usuario por username")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<List<UserRepresentation>> getUserByUsername(
            @Parameter(description = "Username del usuario a buscar", example = "dylan.cadena")
            @PathVariable String username) {
        List<UserRepresentation> users = keyCloakService.searchUserByUsername(username);
        return users.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(users);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario (Keycloak + Local)")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<String> createUser(@RequestBody UserDTO dto) {
        String result = keyCloakService.createUser(dto);

        if (result.contains("Correctamente")) {
            List<UserRepresentation> keycloakUsers = keyCloakService.searchUserByUsername(dto.getUsername());

            if (!keycloakUsers.isEmpty()) {

                String id = keycloakUsers.get(0).getId();
                User user = UserAdapter.fromDTOToUser(dto, id);
                userService.createUser(user);

            }
        }
        return ResponseEntity.ok(result);
    }




    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario en Keycloak y en la base de datos local")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<String> updateUser(
            @PathVariable String id,
            @RequestBody UserDTO userDTO) {

        // Primero, actualizamos el usuario en Keycloak
        try {
            keyCloakService.updateUser(id, userDTO);  // Llamamos al servicio de Keycloak para actualizar
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el usuario en Keycloak: " + e.getMessage());
        }

        // Luego, actualizamos la base de datos local
        Optional<User> existingUser = userService.getUserById(id);  // Buscamos el usuario en la base de datos local
        if (existingUser.isPresent()) {
            // Convertimos el DTO en un objeto User
            User updatedUser = UserAdapter.fromDTOToUser(userDTO, id);

            // Actualizamos el usuario en la base de datos local
            userService.updateUser(id, updatedUser);  // Actualizamos el usuario con los datos del DTO
            return ResponseEntity.ok("Usuario actualizado correctamente");
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado en la base de datos local");
        }
    }




    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario (Keycloak + Local)")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "ID del usuario local", example = "1")
            @PathVariable String id) {

        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) return ResponseEntity.notFound().build();

        User user = userOptional.get();
        try {
            keyCloakService.deleteUser(id); // Asegúrate que `User` tenga `keycloakId`
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Usuario no encontrado en Keycloak");
        }

        userService.deleteUser(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}
