package ru.practicum.booking;

import java.util.List;

public interface BookingService {
    BookingDto postBooking(Long userId, BookingDto request);

    BookingDto patchApproved(Long userId,Long bookingId, String approved);

    BookingDto getInfo(Long userId,Long bookingId);

    List<BookingDto> getBookings(Long userId, String state);

    List<BookingDto> getBookingOwner(Long userId, String state);
}