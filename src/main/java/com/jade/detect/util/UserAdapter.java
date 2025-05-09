package com.jade.detect.util;

import com.jade.detect.model.UserDTO;
import com.jade.detect.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
@Slf4j
public class UserAdapter {

    public static User fromDTOToUser(UserDTO dto, String id) {
        User.Role role = dto.getRoles().contains("admin") ? User.Role.ADMIN : User.Role.USER;
        return new User(
                id,
                dto.getUsername(),
                dto.getEmail(),
                role
        );
    }
}