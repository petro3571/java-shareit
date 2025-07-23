package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto saveUser(NewUserDto user);

//    UserDto updateUser(UpdateUserRequest user, Long userId);

    UserDto getUserById(Long userId);

    UserDto deleteUser(Long userid);
}
