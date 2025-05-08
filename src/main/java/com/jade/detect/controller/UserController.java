package com.jade.detect.controller;

import com.jade.detect.model.User;
import com.jade.detect.service.IKeyCloakService;
import com.jade.detect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('admin_client_role')")
@Tag(name = "Usuarios", description = "Gestión de usuarios en el sistema")
public class UserController {

    private final UserService userService;
    @Autowired
    private IKeyCloakService keyCloakService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios",
            description = "Recupera la lista de todos los usuarios registrados en el sistema.")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(keyCloakService.findAllUsers());
    }
//    public List<User> getUsers() {
//        return userService.getAllUsers();
//    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Obtener usuario por ID",
            description = "Busca un usuario en la base de datos por su ID.")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID del usuario a buscar", example = "1")
            @PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Obtener usuario por nombre",
            description = "Busca un usuario en la base de datos por su nombre.")
    public ResponseEntity<User> getUserByName(
            @Parameter(description = "Nombre del usuario a buscar", example = "Dylan Cadena")
            @PathVariable String username) {
        Optional<User> user = userService.getUserByName(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Agregar un nuevo usuario",
            description = "Crea y guarda un nuevo usuario en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a registrar",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de usuario",
                                    value = """
                                        {
                                          "username": "Juan Pérez",
                                          "email": "juan.perez@example.com",
                                          "role": "USER",
                                          "password": "123456"
                                        }
                                        """
                            )
                    )
            )
    )
    public User addUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar un usuario por ID",
            description = "Permite actualizar uno o más campos del usuario usando su ID.")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID del usuario a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody User partialUser) {
        Optional<User> updatedUser = userService.updateUser(id, partialUser);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario",
            description = "Elimina un usuario del sistema por su ID.")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
