package ru.practicum.shareit.item.model;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long id;
    private Long ownerId;
    private String name;
    private String description;
    private boolean available;;
}
