package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.util.List;


public interface BookingService {
    BookingDto postBooking(Long userId, NewBookingRequest request);

    BookingDto patchApproved(Long userId,Long bookingId, Boolean approved);

    BookingDto getInfo(Long userId,Long bookingId);

    List<BookingDto> getBookings(Long userId, String state);

    List<BookingDto> getBookingOwner(Long userId, String state);
}