package ru.practicum.booking;

import ru.practicum.item.ItemMapper;
import ru.practicum.user.UserMapper;

public class BookingMapper {
    public static Booking mapToBooking(BookingDto request) {
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

    public static Booking mapToBookingFromWithDate(BookingDtoWithDate dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setStartDate(dto.getStart());
        booking.setEndDate(dto.getEnd());
        return booking;
    }
}
