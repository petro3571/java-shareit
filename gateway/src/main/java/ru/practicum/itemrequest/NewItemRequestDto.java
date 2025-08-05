package ru.practicum.itemrequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewItemRequestDto {
    @NotBlank
    private String description;
}