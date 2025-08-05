package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.comment.CommentDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    public ItemDto postItem(@RequestHeader(REQUEST_HEADER) long userId,
                            @RequestBody ItemDto item) {
        return itemService.addNewItem(userId, item);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader(REQUEST_HEADER) long userId) {
        return itemService.getItems(userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(REQUEST_HEADER) long userId,
                           @PathVariable(name = "itemId") long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestHeader(REQUEST_HEADER) long userId,
                             @PathVariable(name = "itemId") long itemId,
                             @RequestBody ItemDto item) {
        return itemService.patchItem(userId,itemId,item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getUserItem(@RequestHeader(REQUEST_HEADER) long userId,
                               @PathVariable(name = "itemId") long itemId) {
        return itemService.getItem(userId,itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(REQUEST_HEADER) long userId,
                                     @RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader(REQUEST_HEADER) long userId,
                                  @PathVariable(name = "itemId") long itemId,
                                  @RequestBody CommentDto comment) {
        return itemService.postComment(userId, itemId, comment);
    }
}