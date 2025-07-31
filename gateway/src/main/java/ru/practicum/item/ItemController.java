package ru.practicum.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.comment.NewCommentDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> postItem(@RequestHeader(REQUEST_HEADER) long userId,
                                           @Valid @RequestBody NewItemDto item) {
        return itemClient.createItem(userId, item);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader(REQUEST_HEADER) long userId) {
        return itemClient.getAllUserItems(userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(REQUEST_HEADER) long userId,
                           @PathVariable(name = "itemId") @Min(1) long itemId) {
        itemClient.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader(REQUEST_HEADER) long userId,
                             @PathVariable(name = "itemId") @Min(1) long itemId,
                             @Valid @RequestBody UpdateItemDto item) {
        return itemClient.updateItem(userId,itemId,item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getUserItem(@RequestHeader(REQUEST_HEADER) long userId,
                                                  @PathVariable(name = "itemId") @Min(1) long itemId) {
        return itemClient.getUserItem(itemId,userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(REQUEST_HEADER) long userId,
                                     @RequestParam String text) {
        return itemClient.searchItems(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader(REQUEST_HEADER) long userId,
                                  @PathVariable(name = "itemId") @Min(1) long itemId,
                                  @Valid @RequestBody NewCommentDto comment) {
        return itemClient.postComment(userId, itemId, comment);
    }
}