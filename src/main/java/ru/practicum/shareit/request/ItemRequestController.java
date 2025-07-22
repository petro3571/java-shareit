package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithResponses;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto postItemRequest(@RequestHeader(REQUEST_HEADER) @Min(1) long userId,
                                          @Valid @RequestBody NewItemRequestDto request) {
        return service.saveRequest(userId, request);
    }

    @GetMapping
    public List<ItemRequestDtoWithResponses> getOwnerRequests(@RequestHeader(REQUEST_HEADER) @Min(1) long userId) {
        return service.getOwnerRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoWithResponses> getAllRequests(@RequestHeader(REQUEST_HEADER) @Min(1) long userId) {
        return service.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithResponses getRequest(@PathVariable(name = "requestId") Long requestId,
                                                  @RequestHeader(REQUEST_HEADER) @Min(1) long userId) {
        return service.getRequest(requestId, userId);
    }
}
