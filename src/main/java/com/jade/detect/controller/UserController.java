package com.jade.detect.controller;

import com.jade.detect.model.Device;
import com.jade.detect.model.Log;
import com.jade.detect.model.UserDTO;
import com.jade.detect.model.User;
import com.jade.detect.repository.IKeyCloakRepository;
import com.jade.detect.service.DeviceService;
import com.jade.detect.service.LogService;
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
    private final LogService logService;
    private final DeviceService deviceService;

    @Autowired
    public UserController(UserService userService, IKeyCloakRepository keyCloakService,
                          LogService logService, DeviceService deviceService) {
        this.userService = userService;
        this.keyCloakService = keyCloakService;
        this.logService = logService;
        this.deviceService = deviceService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios de Keycloak")
    public ResponseEntity<List<UserRepresentation>> getUsers() {
        return ResponseEntity.ok(keyCloakService.findAllUsers());
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Buscar usuario por ID de Keycloak")
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
    public ResponseEntity<List<UserRepresentation>> getUserByUsername(
            @Parameter(description = "Username del usuario a buscar", example = "dylan.cadena")
            @PathVariable String username) {
        List<UserRepresentation> users = keyCloakService.searchUserByUsername(username);
        return users.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(users);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario (Keycloak + Local)")
    public ResponseEntity<?> createUser(@RequestBody UserDTO dto) {
        String result = keyCloakService.createUser(dto);

        String id = null;
        if (result.contains("Correctamente")) {
            List<UserRepresentation> keycloakUsers = keyCloakService.searchUserByUsername(dto.getUsername());
            if (!keycloakUsers.isEmpty()) {
                id = keycloakUsers.get(0).getId();
                User user = UserAdapter.fromDTOToUser(dto, id);
                userService.createUser(user);
                registrarLog(Log.LogLevel.INFO, "Usuario creado exitosamente: " + user.getUsername());
            }
        }

        // Devuelve tanto el mensaje como el id (si existe)
        return ResponseEntity.ok(
            id != null
                ? java.util.Map.of("message", result, "id", id)
                : java.util.Map.of("message", result)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario en Keycloak y en la base de datos local")
    public ResponseEntity<String> updateUser(
            @PathVariable String id,
            @RequestBody UserDTO userDTO) {

        try {
            keyCloakService.updateUser(id, userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el usuario en Keycloak: " + e.getMessage());
        }

        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            User updatedUser = UserAdapter.fromDTOToUser(userDTO, id);
            userService.updateUser(id, updatedUser);

            registrarLog(Log.LogLevel.INFO, "Usuario actualizado: " + updatedUser.getUsername());
            return ResponseEntity.ok("Usuario actualizado correctamente");
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado en la base de datos local");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario (Keycloak + Local)")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "ID del usuario local", example = "1")
            @PathVariable String id) {

        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) return ResponseEntity.notFound().build();

        User user = userOptional.get();
        try {
            keyCloakService.deleteUser(id);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Usuario no encontrado en Keycloak");
        }

        registrarLog(Log.LogLevel.WARNING, "Usuario eliminado: " + user.getUsername());

        userService.deleteUser(id);

        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    @PostMapping("/{id}/send-verify-email")
    @Operation(summary = "Enviar correo de verificación a un usuario de Keycloak")
    public ResponseEntity<String> sendVerificationEmail(@PathVariable String id) {
        try {
            keyCloakService.sendVerificationEmail(id);
            return ResponseEntity.ok("Correo de verificación enviado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar correo de verificación: " + e.getMessage());
        }
    }

    // Método privado reutilizable para registrar logs
    private void registrarLog(Log.LogLevel level, String mensaje) {

        Log log = new Log();
        log.setLevel(level);
        log.setMessage(mensaje);

        logService.saveLog(log);
    }
}
