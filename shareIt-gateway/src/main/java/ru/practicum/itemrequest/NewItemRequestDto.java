package ru.practicum.itemrequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewItemRequestDto {
    @NotBlank
    private String description;
}
