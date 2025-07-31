package ru.practicum.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> postBooking(@RequestHeader(REQUEST_HEADER) @Min(1) long userId,
                                              @Valid @RequestBody NewBookingDto booking) {
        return bookingClient.postBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> patchItem(@RequestHeader(REQUEST_HEADER) @Min(1) long userId,
                                @PathVariable(name = "bookingId") long bookingId,
                                @RequestParam String approved) {
        return bookingClient.patchApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getInfoBooking(@RequestHeader(REQUEST_HEADER) @Min(1) long userId,
                                     @PathVariable(name = "bookingId") long bookingId) {
        return bookingClient.getInfo(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(REQUEST_HEADER) @Min(1) long userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsOwner(@RequestHeader(REQUEST_HEADER) @Min(1) long userId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.getBookingOwner(userId, state);
    }
}