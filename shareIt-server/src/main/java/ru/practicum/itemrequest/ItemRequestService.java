package ru.practicum.itemrequest;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequestDtoWithResponses> getAllRequests(Long userId);

    ItemRequestDtoWithResponses saveRequest(Long userId, ItemRequestDtoWithResponses itemRequestDto);

    List<ItemRequestDtoWithResponses> getOwnerRequests(Long userId);

    ItemRequestDtoWithResponses getRequest(Long requestId, Long userId);

}
