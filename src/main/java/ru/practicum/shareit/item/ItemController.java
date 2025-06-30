package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.exceptions.NotFoundUserForItemException;
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
    private final ItemService itemService;

    @PostMapping
    public ItemDto postItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @Valid @RequestBody NewItemRequest item) {
        return itemService.addNewItem(userId, item);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(name = "itemId") long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                             @PathVariable(name = "itemId") long itemId,
                             @Valid @RequestBody UpdateItemRequest item) {
        return itemService.patchItem(userId,itemId,item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getUserItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable(name = "itemId") long itemId) {
        return itemService.getItem(userId,itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestParam String text) {
        return itemService.searchItems(text);
    }

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundUser(NotFoundUserException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(NotFoundUserForItemException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleNotFoundUser(NotFoundUserForItemException ex) {
        return ex.getMessage();
    }
}