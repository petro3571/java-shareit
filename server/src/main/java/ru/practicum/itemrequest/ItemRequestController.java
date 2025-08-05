package ru.practicum.itemrequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<ItemRequestDtoWithResponses> getAllRequests(@RequestHeader(REQUEST_HEADER) long userId,
                                                            @RequestParam(required = false, defaultValue = "0") int pageNum,
                                                            @RequestParam(required = false, defaultValue = "10") int limit,
                                                            @RequestParam(required = false, defaultValue = "created") String sortBy) {
        Pageable pageable = PageRequest.of(pageNum, limit, Sort.by(sortBy));

        return service.getAllRequests(userId, pageable);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithResponses getRequest(@PathVariable(name = "requestId") Long requestId,
                                                  @RequestHeader(REQUEST_HEADER) long userId) {
        return service.getRequest(requestId, userId);
    }
}