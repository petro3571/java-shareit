package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.exceptions.NotFoundUserForItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItems(Long userId) {
        return itemRepository.findByUserId(userId).stream().map(ItemMapper::mapToItemDto).toList();
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        log.info("Запрос на получение данных о вещи от пользователя с id = {}", userId);
        return ItemMapper.mapToItemDto(itemRepository.getItem(itemId));
    }

    @Override
    public ItemDto addNewItem(Long userId, NewItemRequest request) {
        if (!userRepository.findAll().stream().filter(user -> user.getId()==userId).findFirst().isPresent()) {
            throw new NotFoundUserException("Пользователя с id = " + userId + " нет.");
        }

        Item item = ItemMapper.mapToItem(request);
        item.setOwnerId(userId);
        item = itemRepository.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        if (!userRepository.findAll().stream().filter(user -> user.getId()==userId).findFirst().isPresent()) {
            throw new NotFoundException("Такого Пользователя нет.");
        }

        if (itemRepository.getItem(itemId).getOwnerId() == userId) {
            itemRepository.deleteByUserIdAndItemId(userId, itemId);
        }
    }

    @Override
    public ItemDto patchItem(Long userId, Long itemId, UpdateItemRequest request) {
        Item item = ItemMapper.updateItemFields(new Item(), request);

        if (!itemRepository.findByUserId(userId).stream().filter(i -> i.getId() == itemId).findFirst().isPresent()) {
            throw new NotFoundUserForItemException("Такой вещи у пользователя нет.");
        }

        if (!userRepository.findAll().stream().filter(user -> user.getId()==userId).findFirst().isPresent()) {
            throw new NotFoundException("Такого Пользователя нет.");
        }

        item.setId(itemId);
        item.setOwnerId(userId);
        System.out.println(item);
        return ItemMapper.mapToItemDto(itemRepository.patchItem(item));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        return itemRepository.searchItems(text).stream().map(ItemMapper::mapToItemDto).toList();
    }
}