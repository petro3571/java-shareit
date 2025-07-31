package ru.practicum.item.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewCommentDto {
    @NotBlank
    private String text;
}