package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDateAndComments;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item mapToItem(NewItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvailable());
        return dto;
    }

    public static Item updateItemFields(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }

        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }

            item.setAvailable(request.isAvailable());
        return item;
    }

    public static ItemDtoWithDateAndComments mapToItemFromBooking(Booking booking) {
        ItemDtoWithDateAndComments item = new ItemDtoWithDateAndComments();
        item.setId(booking.getItem().getId());
        item.setName(booking.getItem().getName());
        item.setDescription(booking.getItem().getDescription());
        item.setAvailable(booking.getItem().isAvailable());

        return item;
    }

    public static ItemDtoWithDateAndComments mapToItemFromItem(Item item) {
        ItemDtoWithDateAndComments itemDtoWithDateAndComments = new ItemDtoWithDateAndComments();
        itemDtoWithDateAndComments.setId(item.getId());
        itemDtoWithDateAndComments.setName(item.getName());
        itemDtoWithDateAndComments.setDescription(item.getDescription());
        itemDtoWithDateAndComments.setAvailable(item.isAvailable());

        return itemDtoWithDateAndComments;
    }
}