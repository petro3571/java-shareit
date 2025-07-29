package ru.practicum.itemrequest;

import ru.practicum.user.UserMapper;

public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(ItemRequestDtoWithResponses request) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(request.getDescription());

        return itemRequest;
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
