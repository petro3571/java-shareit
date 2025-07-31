package ru.practicum.user;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto saveUser(UserDto user);

    UserDto updateUser(UserDto user, Long userId);

    UserDto getUserById(Long userId);

    UserDto deleteUser(Long userid);
}