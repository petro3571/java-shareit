package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDtoWithDateAndComments {

    private Long id;

    private String name;

    private String description;

    private boolean available;

    private BookingDtoWithDate lastBooking;

    private BookingDtoWithDate nextBooking;

    private List<CommentDto> comments = new ArrayList<>();
}