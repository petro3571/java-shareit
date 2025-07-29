package ru.practicum.item;

import ru.practicum.item.comment.CommentDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItems(Long userId);

    ItemDto addNewItem(Long userId, ItemDto item);

    void deleteItem(Long userId, Long itemId);

    ItemDto patchItem(Long userId, Long itemId, ItemDto request);

    ItemDto getItem(Long userId, Long itemId);

    List<ItemDto> searchItems(String text);

    CommentDto postComment(Long userId, Long itemId, CommentDto request);
}
