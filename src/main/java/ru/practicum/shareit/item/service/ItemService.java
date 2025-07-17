package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.NewCommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDateAndComments;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItems(Long userId);

    ItemDto addNewItem(Long userId, NewItemRequest item);

    void deleteItem(Long userId, Long itemId);

    ItemDto patchItem(Long userId, Long itemId, UpdateItemRequest request);

    ItemDtoWithDateAndComments getItem(Long userId, Long itemId);

    List<ItemDto> searchItems(String text);

    CommentDto postComment(Long userId, Long itemId, NewCommentRequest request);
}