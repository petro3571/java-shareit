package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto saveUser(NewUserRequest user);

    UserDto updateUser(UpdateUserRequest user, Long userId);

    UserDto getUserById(Long userId);

    UserDto deleteUser(Long userid);
}