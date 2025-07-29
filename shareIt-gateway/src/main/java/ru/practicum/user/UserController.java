package ru.practicum.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable("userId") @Min(1) Long userId) {
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveNewUser(@Valid @RequestBody NewUserDto user) {
        return userClient.createUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UpdateUserDto user,
                                             @PathVariable("userId") @Min(1) Long userId) {
        return userClient.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") @Min(1) Long userId) {
        return userClient.deleteUser(userId);
    }
}
