package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
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
    public UserDto saveNewUser(@RequestBody UserDto user) {
        return userService.saveUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto user,
                              @PathVariable("userId") Long userId) {
        return userService.updateUser(user,userId);
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable("userId") Long userId) {
        return userService.deleteUser(userId);
    }
}