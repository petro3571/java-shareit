package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto saveUser(NewUserRequest user);
    UserDto updateUser(UpdateUserRequest user,Long userId);
    UserDto getUserById(Long userId);
    UserDto deleteUser(Long userid);
}