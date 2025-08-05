package ru.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.ItemDto;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDto item;

    private String itemName;

    private UserDto booker;

    private BookingStatus status;

    private Long itemId;
}