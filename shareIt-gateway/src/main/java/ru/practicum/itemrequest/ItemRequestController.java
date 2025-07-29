package ru.practicum.itemrequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.ItemClient;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> postItemRequest(@RequestHeader(REQUEST_HEADER) @Min(1) long userId,
                                                  @Valid @RequestBody NewItemRequestDto request) {
        return client.saveRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerRequests(@RequestHeader(REQUEST_HEADER) @Min(1) long userId) {
        return client.getOwnerRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(REQUEST_HEADER) @Min(1) long userId) {
        return client.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object>  getRequest(@PathVariable(name = "requestId") Long requestId,
                                                  @RequestHeader(REQUEST_HEADER) @Min(1) long userId) {
        return client.getRequest(requestId, userId);
    }
}

