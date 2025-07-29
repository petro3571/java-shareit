package ru.practicum.item.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewCommentDto {
    @NotBlank
    private String text;

//    private LocalDateTime created;
}