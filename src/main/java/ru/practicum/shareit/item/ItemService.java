package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItems(Long userId);
    ItemDto addNewItem(Long userId, NewItemRequest item);
    void deleteItem(Long userId, Long itemId);
    ItemDto patchItem(Long userId, Long itemId, UpdateItemRequest request);
    ItemDto getItem(Long userId, Long itemId);
    List<ItemDto> searchItems(String text);
}