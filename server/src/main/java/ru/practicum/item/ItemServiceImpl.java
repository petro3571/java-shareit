package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.BookingStatus;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.NotFoundUserException;
import ru.practicum.exceptions.NotFoundUserForItemException;
import ru.practicum.item.comment.Comment;
import ru.practicum.item.comment.CommentDto;
import ru.practicum.item.comment.CommentMapper;
import ru.practicum.item.comment.CommentRepository;
import ru.practicum.itemrequest.ItemRequest;
import ru.practicum.itemrequest.ItemRequestRepo;
import ru.practicum.itemrequest.responsetoitemreq.ResponseToItemReq;
import ru.practicum.itemrequest.responsetoitemreq.ResponseToItemReqRepo;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
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
    public ItemDto getItem(Long userId, Long itemId) {
        log.info("Запрос на получение данных о вещи от пользователя с id = {}", userId);
        Item item = itemRepository.getById(itemId);
        ItemDto newItem = ItemMapper.mapToItemDto(item);

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
        newItem.setNextBooking(bookingRepository.findFirstByItemIdAndStartAfter(itemId, LocalDateTime.now().plusHours(8)));
        return newItem;

    }

    @Override
    public ItemDto addNewItem(Long userId, ItemDto request) {
        if (!userRepository.findAll().stream().filter(user -> user.getId().equals(userId)).findFirst().isPresent()) {
            throw new NotFoundUserException("Пользователя с id = " + userId + " нет.");
        }

        Item item = ItemMapper.mapToItem(request);
        User findUser = userRepository.getById(userId);
        item.setOwner(findUser);

        item = itemRepository.save(item);

        if (item.getRequestId() != null) {
            if (itemRequestRepository.getById(item.getRequestId()) != null) {
                ItemRequest itemRequest = itemRequestRepository.getById(item.getRequestId());
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
    public ItemDto patchItem(Long userId, Long itemId, ItemDto request) {
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
    public CommentDto postComment(Long userId, Long itemId, CommentDto request) {
        if (!userRepository.findAll().stream().filter(user -> user.getId().equals(userId)).findFirst().isPresent()) {
            throw new NotFoundException("Такого Пользователя нет.");
        }
        Booking booking = bookingRepository.findByBooker_IdAndItem_Id(userId, itemId);

        if (booking.equals(null) || booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new NotFoundException("Пользователь не является арендатором вещи.");
        } else {
            if (booking.getEndDate().isBefore(LocalDateTime.now().plusHours(8))) {
                Comment comment = CommentMapper.mapToComment(request);
                comment.setCreated(LocalDateTime.now().plusHours(8));
                comment.setItem(itemRepository.getById(itemId));
                comment.setAuthor(userRepository.getById(userId));
                comment = commentRepository.save(comment);
                return CommentMapper.mapToCommentDto(comment);
            } else {
                throw new RuntimeException("Пользователь может оставить отзыва только после окончания срока аренды");
            }
        }
    }
}