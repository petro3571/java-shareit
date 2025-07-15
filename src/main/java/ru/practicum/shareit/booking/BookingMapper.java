package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;


public class BookingMapper {
    public static Booking mapToBooking(NewBookingRequest request) {
        Booking booking = new Booking();
        booking.setStartDate(request.getStart());
        booking.setEndDate(request.getEnd());
        booking.setStatus(request.getStatus());
        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStartDate());
        dto.setEnd(booking.getEndDate());
        dto.setStatus(booking.getStatus());
        dto.setItem(ItemMapper.mapToItemDto(booking.getItem()));
        dto.setBooker(UserMapper.mapToUserDto(booking.getBooker()));
        dto.setItemName(booking.getItem().getName());
        return dto;
    }
}