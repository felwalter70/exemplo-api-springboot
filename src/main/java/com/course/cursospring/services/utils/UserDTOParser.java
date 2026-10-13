package com.course.cursospring.services.utils;

import com.course.cursospring.domain.User;
import com.course.cursospring.domain.dto.UserCreateDTO;
import com.course.cursospring.domain.dto.UserUpdateDTO;

import jakarta.validation.Valid;

public abstract class UserDTOParser {
    public static User fromDTO(@Valid UserCreateDTO obj) {
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setPassword(obj.getPassword());

        return user;
    }

    public static User fromDTO(@Valid UserUpdateDTO obj) {
        User user = new User();
        user.setId(obj.getId());
        user.setPassword(obj.getPassword());

        return user;
    }
}
