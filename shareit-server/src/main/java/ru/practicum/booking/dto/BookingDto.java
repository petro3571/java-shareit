package ru.practicum.booking.dto;

import lombok.Data;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDto item;

    private String itemName;

    private UserDto booker;

    private BookingStatus status;
}
