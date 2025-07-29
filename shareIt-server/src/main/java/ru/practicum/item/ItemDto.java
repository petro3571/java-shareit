package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.booking.BookingDtoWithDate;
import ru.practicum.item.comment.CommentDto;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private boolean available;

    private BookingDtoWithDate lastBooking;

    private BookingDtoWithDate nextBooking;

    private List<CommentDto> comments = new ArrayList<>();

    private Long requestId;
}