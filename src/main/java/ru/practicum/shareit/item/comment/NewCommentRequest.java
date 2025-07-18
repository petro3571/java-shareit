package ru.practicum.shareit.item.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewCommentRequest {
    @NotBlank
    private String text;

    private LocalDateTime created = LocalDateTime.now();
}
