package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithResponses;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequestDtoWithResponses> getAllRequests(Long userId);

    ItemRequestDto saveRequest(Long userId, NewItemRequestDto itemRequestDto);

    List<ItemRequestDtoWithResponses> getOwnerRequests(Long userId);

    ItemRequestDtoWithResponses getRequest(Long requestId, Long userId);

}
