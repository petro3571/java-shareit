package ru.practicum.item;

public class ItemMapper {
    public static Item mapToItem(ItemDto request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.isAvailable());

        if (request.getRequestId() != null) {
            item.setRequestId(request.getRequestId());
        }

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

    public static Item updateItemFields(Item item, ItemDto request) {
        if (!(request.getName() == null || request.getName().isBlank())) {
            item.setName(request.getName());
        }

        if (!(request.getDescription() == null || request.getDescription().isBlank())) {
            item.setDescription(request.getDescription());
        }

        item.setAvailable(request.isAvailable());
        return item;
    }

//    public static ItemDtoWithDateAndComments mapToItemFromBooking(Booking booking) {
//        ItemDtoWithDateAndComments item = new ItemDtoWithDateAndComments();
//        item.setId(booking.getItem().getId());
//        item.setName(booking.getItem().getName());
//        item.setDescription(booking.getItem().getDescription());
//        item.setAvailable(booking.getItem().isAvailable());
//
//        return item;
//    }

//    public static ItemDto mapToItemWithCommentsFromItem(Item item) {
//        ItemDto itemDtoWithDateAndComments = new ItemDto();
//        itemDtoWithDateAndComments.setId(item.getId());
//        itemDtoWithDateAndComments.setName(item.getName());
//        itemDtoWithDateAndComments.setDescription(item.getDescription());
//        itemDtoWithDateAndComments.setAvailable(item.isAvailable());
//
//        return itemDtoWithDateAndComments;
//    }
}