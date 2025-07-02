package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    public ItemDto postItem(@RequestHeader(REQUEST_HEADER) long userId,
                                            @Valid @RequestBody NewItemRequest item) {
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
                             @Valid @RequestBody UpdateItemRequest item) {
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
}