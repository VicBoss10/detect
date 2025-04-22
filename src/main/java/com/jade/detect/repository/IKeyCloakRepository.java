package com.jade.detect.repository;

import com.jade.detect.model.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeyCloakRepository {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);
    String createUser(UserDTO userDTO);
    void deleteUser (String userId);
    void updateUser (String userId, UserDTO userDTO);
    UserRepresentation findUserById(String keycloakId);

}
