package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public BookingDto postBooking(@RequestHeader(REQUEST_HEADER) long userId,
                                  @RequestBody BookingDto booking) {
        return bookingService.postBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patchItem(@RequestHeader(REQUEST_HEADER) long userId,
                                @PathVariable(name = "bookingId") long bookingId,
                                @RequestParam String approved) {
        return bookingService.patchApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getInfoBooking(@RequestHeader(REQUEST_HEADER) long userId,
                                     @PathVariable(name = "bookingId") long bookingId) {
        return bookingService.getInfo(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader(REQUEST_HEADER) long userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsOwner(@RequestHeader(REQUEST_HEADER) long userId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingOwner(userId, state);
    }
}