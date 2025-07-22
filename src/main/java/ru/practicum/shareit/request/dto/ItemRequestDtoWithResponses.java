package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.responsetoitemreq.ResponseToItemReqDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRequestDtoWithResponses {
    private Long id;

    private String description;

    private LocalDateTime created;

    private UserDto user;

    private List<ItemDto> items;

    private List<ResponseToItemReqDto> responses = new ArrayList<>();
}
