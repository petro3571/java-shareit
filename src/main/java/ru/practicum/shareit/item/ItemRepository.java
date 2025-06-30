package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);
    List<Item> findByUserId(Long userId);
    void deleteByUserIdAndItemId(Long userId, Long itemId);
    Item patchItem(Item item);
    Item getItem(Long itemId);
    List<Item> searchItems(String text);
}
