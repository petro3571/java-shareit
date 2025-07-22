package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.exceptions.NotFoundUserForItemException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.NewItemRepo;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDateAndComments;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepo;
import ru.practicum.shareit.request.responsetoitemreq.ResponseToItemReq;
import ru.practicum.shareit.request.responsetoitemreq.ResponseToItemReqRepo;
import ru.practicum.shareit.user.repository.NewUserRepo;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final NewUserRepo userRepository;
    private final NewItemRepo itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepo itemRequestRepository;
    private final ResponseToItemReqRepo responseRepository;

    @Override
    public List<ItemDto> getItems(Long userId) {
        return itemRepository.findByOwner_id(userId).stream().map(ItemMapper::mapToItemDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDtoWithDateAndComments getItem(Long userId, Long itemId) {
        log.info("Запрос на получение данных о вещи от пользователя с id = {}", userId);
        Item item = itemRepository.getById(itemId);
        ItemDtoWithDateAndComments newItem = ItemMapper.mapToItemFromItem(item);

        if (commentRepository.findByItemId(itemId) != null) {
            CommentDto commentForItem = CommentMapper.mapToCommentDto(commentRepository.findByItemId(itemId));
            newItem.getComments().add(commentForItem);
        }

        if (bookingRepository.findFirstByItemIdAndEndBeforeNow(itemId) != null
                && bookingRepository.findFirstByItemIdAndEndBeforeNow(itemId).getEnd().isBefore(LocalDateTime.now().minusMinutes(1L))) {
            newItem.setLastBooking(bookingRepository.findFirstByItemIdAndEndBeforeNow(itemId));
        } else {
            newItem.setLastBooking(null);
        }
        newItem.setNextBooking(bookingRepository.findFirstByItemIdAndStartAfterNow(itemId));
        return newItem;

    }

    @Override
    public ItemDto addNewItem(Long userId, NewItemRequest request) {
        if (!userRepository.findAll().stream().filter(user -> user.getId().equals(userId)).findFirst().isPresent()) {
            throw new NotFoundUserException("Пользователя с id = " + userId + " нет.");
        }

        Item item = ItemMapper.mapToItem(request);
        User findUser = userRepository.getById(userId);
        item.setOwner(findUser);

        item = itemRepository.save(item);

        if (item.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.getById(item.getRequestId());

            if (itemRequest != null) {
                ResponseToItemReq response = new ResponseToItemReq();
                response.setItemRequest(itemRequest);
                response.setItem(item);
                responseRepository.save(response);
            }
        }
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        if (!userRepository.findAll().stream().filter(user -> user.getId().equals(userId)).findFirst().isPresent()) {
            throw new NotFoundException("Такого Пользователя нет.");
        }

        if (itemRepository.getById(itemId).getOwner().getId().equals(userId)) {
            Item item = itemRepository.getById(itemId);
            itemRepository.delete(item);
        }
    }

    @Override
    public ItemDto patchItem(Long userId, Long itemId, UpdateItemRequest request) {
        Item item = ItemMapper.updateItemFields(new Item(), request);

        if (!itemRepository.findAll().stream().filter(i -> i.getId().equals(itemId) && i.getOwner().getId().equals(userId))
                .findFirst().isPresent()) {
            throw new NotFoundUserForItemException("Такой вещи у пользователя нет.");
        }

        if (!userRepository.findAll().stream().filter(user -> user.getId().equals(userId)).findFirst().isPresent()) {
            throw new NotFoundException("Такого Пользователя нет.");
        }

        item.setId(itemId);
        User findUser = userRepository.getById(userId);
        item.setOwner(findUser);
        System.out.println(item);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository.searchItemBy(text).stream().map(ItemMapper::mapToItemDto).toList();
    }

    @Override
    public CommentDto postComment(Long userId, Long itemId, NewCommentRequest request) {
        if (!userRepository.findAll().stream().filter(user -> user.getId().equals(userId)).findFirst().isPresent()) {
            throw new NotFoundException("Такого Пользователя нет.");
        }

        if (bookingRepository.findByBooker_Id(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не является арендатором вещи.");
        } else {
            if (!bookingRepository.findByBooker_Id(userId).stream()
                    .filter(booking -> booking.getEndDate().isBefore(LocalDateTime.now())
                            && booking.getItem().getId().equals(itemId)).findFirst().isPresent()) {
                throw new RuntimeException("Пользователь может оставить отзыва только после окончания срока аренды");
            } else {
                Comment comment = CommentMapper.mapToComment(request);
                comment.setItem(itemRepository.getById(itemId));
                comment.setAuthor(userRepository.getById(userId));
                comment = commentRepository.save(comment);
                return CommentMapper.mapToCommentDto(comment);
            }
        }
    }
}