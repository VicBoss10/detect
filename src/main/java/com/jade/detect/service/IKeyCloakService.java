package com.jade.detect.service;

import com.jade.detect.controller.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeyCloakService {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);
    String createUser(UserDTO userDTO);
    void deleteUser (String userId);
    void updateUser (String userId, UserDTO userDTO);
}
