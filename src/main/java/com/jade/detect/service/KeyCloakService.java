package com.jade.detect.service;

import com.jade.detect.model.UserDTO;
import com.jade.detect.repository.IKeyCloakRepository;
import com.jade.detect.util.KeyCloakProvider;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@Slf4j

public class KeyCloakService implements IKeyCloakRepository {

    /**
     * Metodo para listar todos los usuarios de KeyCloak
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeyCloakProvider.getRealmResource()
                .users()
                .list();
    }

    /**
     * Metodo para buscar un usuario por el username
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeyCloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }


    /**
     * Metodo para crear un usuario
     */
    @Override
    public String createUser(@NonNull UserDTO userDTO) {

        int status = 0;
        UsersResource userResource = KeyCloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName((userDTO.getLastName()));
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername((userDTO.getUsername()));
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response = userResource.create(userRepresentation);
        status = response.getStatus();

        if (status==201){
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDTO.getPassword());

            userResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = KeyCloakProvider.getRealmResource();

            List<RoleRepresentation> roleRepresentations = null;

            if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()){
                roleRepresentations = List.of(realmResource.roles().get("user")
                        .toRepresentation());
            } else {
                roleRepresentations = realmResource.roles()
                        .list()
                        .stream()
                        .filter(role -> userDTO.getRoles()
                                .stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }

            realmResource.users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(roleRepresentations);

            return "Usuario Creado Correctamente";
            //return userId; // en lugar de "Usuario Creado Correctamente"

        }else if (status == 409){
            log.error(("User exist already"));
            return "El Usuario ya existe";
        }else {
            return "Error creando al usuario";
        }

    }

    /**
     * Metodo para borrar un usuario
     */
    @Override
    public void deleteUser(String userId) {
        KeyCloakProvider.getUserResource()
                .get(userId)
                .remove();
    }

    /**
     * Metodo para actualizar a un usuario
     */
    @Override
    public void updateUser(String userId,@NonNull UserDTO userDTO) {

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName((userDTO.getLastName()));
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername((userDTO.getUsername()));
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource usersResource = KeyCloakProvider.getUserResource().get(userId);
        usersResource.update(userRepresentation);

    }

    @Override
    public UserRepresentation findUserById(String id) {
        try {
            return KeyCloakProvider.getUserResource()
                    .get(id)
                    .toRepresentation();
        } catch (Exception e) {
            log.error("No se pudo obtener el usuario con ID: {}", id, e);
            throw new NotFoundException("Usuario no encontrado en Keycloak");
        }
    }
}
