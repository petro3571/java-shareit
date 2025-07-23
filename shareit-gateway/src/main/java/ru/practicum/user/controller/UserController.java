package ru.practicum.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import ru.practicum.booking.dto.BookItemRequestDto;
import ru.practicum.user.UserClient;
import ru.practicum.user.dto.NewUserRequest;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

//    @GetMapping
//    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
//                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
//                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
//                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
//        BookingState state = BookingState.from(stateParam)
//                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
//        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
//        return bookingClient.getBookings(userId, state, from, size);
//    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody @Valid NewUserRequest requestDto) {
        log.info("Creating requestDto={}", requestDto);
        return userClient.saveUser(requestDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(
                                             @PathVariable Long userId) {
        log.info("Get  userId={}", userId);
        return userClient.getUser(userId);
    }
}
