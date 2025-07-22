package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(NewItemRequestDto request) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(request.getDescription());
        itemRequest.setCreated(request.getCreated());

        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setUser(UserMapper.mapToUserDto(request.getUser()));

        return dto;
    }

    public static ItemRequestDtoWithResponses mapToRequestWithResponses(ItemRequest itemRequest) {
        ItemRequestDtoWithResponses dto = new ItemRequestDtoWithResponses();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(itemRequest.getCreated());
        dto.setUser(UserMapper.mapToUserDto(itemRequest.getUser()));

        return dto;
    }
}