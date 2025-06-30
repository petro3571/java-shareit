package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository{
    private final List<Item> items = new ArrayList<>();

    @Override
    public List<Item> findByUserId(Long userId) {
        return items.stream().filter(item -> item.getOwnerId() == userId).collect(Collectors.toList());
    }

    @Override
    public Item save(Item item) {
        item.setId(getId());
        items.add(item);
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(Long userId, Long itemId) {
        List<Item> findList = findByUserId(userId);
        for (Item item : findList) {
            if (item.getId() == itemId) {
                items.remove(item);
            }
        }
    }

    @Override
    public Item patchItem(Item item) {
        Item itemFromList = items.get(item.getId().intValue() - 1);
            if (item.getName() != null) {
                itemFromList.setName(item.getName());
            }
            if (item.getDescription() != null) {
                itemFromList.setDescription(item.getDescription());
            }
            itemFromList.setAvailable(item.isAvailable());
            return items.set(itemFromList.getId().intValue() - 1, itemFromList);
    }

    @Override
    public Item getItem(Long itemId) {
        return items.get(itemId.intValue() - 1);
    }

    @Override
    public List<Item> searchItems(String text) {
            return items.stream().filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.isAvailable() == true)
                    .toList();
    }

    private long getId() {
        long lastId = items.stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}