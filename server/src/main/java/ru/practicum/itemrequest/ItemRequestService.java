package ru.practicum.itemrequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRequestService {

    Page<ItemRequestDtoWithResponses> getAllRequests(Long userId, Pageable pageable);

    ItemRequestDtoWithResponses saveRequest(Long userId, ItemRequestDtoWithResponses itemRequestDto);

    List<ItemRequestDtoWithResponses> getOwnerRequests(Long userId);

    ItemRequestDtoWithResponses getRequest(Long requestId, Long userId);

}