package ru.practicum.item;

import lombok.Data;

@Data
public class UpdateItemDto {
    private String name;

    private String description;

    private boolean available;
}