package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto saveNewUser(@RequestBody NewUserDto user) {
        return userService.saveUser(user);
    }

//    @PatchMapping("/{userId}")
//    public UserDto updateUser( @RequestBody UpdateUserRequest user,
//                              @PathVariable("userId") Long userId) {
//        return userService.updateUser(user,userId);
//    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable("userId") Long userId) {
        return userService.deleteUser(userId);
    }
}
