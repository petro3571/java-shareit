package ru.practicum.itemrequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDtoWithResponses postItemRequest(@RequestHeader(REQUEST_HEADER) long userId,
                                                       @RequestBody ItemRequestDtoWithResponses request) {
        return service.saveRequest(userId, request);
    }

    @GetMapping
    public List<ItemRequestDtoWithResponses> getOwnerRequests(@RequestHeader(REQUEST_HEADER) long userId) {
        return service.getOwnerRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoWithResponses> getAllRequests(@RequestHeader(REQUEST_HEADER) long userId) {
        return service.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithResponses getRequest(@PathVariable(name = "requestId") Long requestId,
                                                  @RequestHeader(REQUEST_HEADER) long userId) {
        return service.getRequest(requestId, userId);
    }
}