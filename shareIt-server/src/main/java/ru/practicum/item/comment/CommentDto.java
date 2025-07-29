package ru.practicum.item.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;

    private String text;

    private LocalDateTime created;

    private String authorName;
}